package lk.kmart_super.asset.purchase_order.controller;

import lk.kmart_super.asset.common_asset.service.CommonService;
import lk.kmart_super.asset.item.entity.Item;
import lk.kmart_super.asset.item.service.ItemService;
import lk.kmart_super.asset.purchase_order.entity.PurchaseOrder;
import lk.kmart_super.asset.purchase_order.entity.enums.PurchaseOrderPriority;
import lk.kmart_super.asset.purchase_order.entity.enums.PurchaseOrderStatus;
import lk.kmart_super.asset.purchase_order.service.PurchaseOrderService;
import lk.kmart_super.asset.purchase_order_item.entity.PurchaseOrderItem;
import lk.kmart_super.asset.purchase_order_item.service.PurchaseOrderItemService;
import lk.kmart_super.asset.supplier.entity.Supplier;
import lk.kmart_super.asset.supplier.service.SupplierService;
import lk.kmart_super.asset.supplier_item.controller.SupplierItemController;
import lk.kmart_super.util.service.EmailService;
import lk.kmart_super.util.service.MakeAutoGenerateNumberService;
import lk.kmart_super.util.service.TwilioMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderItemService purchaseOrderItemService;
    private final SupplierService supplierService;
    private final CommonService commonService;
    private final ItemService itemService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
    private final EmailService emailService;
    private final TwilioMessageService twilioMessageService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
                                   PurchaseOrderItemService purchaseOrderItemService, SupplierService supplierService
            , CommonService commonService, ItemService itemService,MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                                   EmailService emailService,
                                   TwilioMessageService twilioMessageService) {
        this.purchaseOrderService = purchaseOrderService;
      this.purchaseOrderItemService = purchaseOrderItemService;
      this.supplierService = supplierService;
        this.commonService = commonService;
      this.itemService = itemService;
      this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
        this.emailService = emailService;
        this.twilioMessageService = twilioMessageService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.findAll());
        model.addAttribute("heading", "All Purchase Order");
        model.addAttribute("grnStatus", false);
        return "purchaseOrder/purchaseOrder";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("purchaseOrder", new Supplier());
        model.addAttribute("searchAreaShow", true);
        return "purchaseOrder/addPurchaseOrder";
    }

    @PostMapping("/find")
    public String search(@Valid @ModelAttribute Supplier supplier, Model model) {
        return commonService.purchaseOrder(supplier, model, "purchaseOrder/addPurchaseOrder");
    }


    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("purchaseOrderDetail", purchaseOrderService.findById(id));
        return "purchaseOrder/purchaseOrder-detail";

    }

    @PostMapping("/save")
    public String purchaseOrderPersist(@Valid @ModelAttribute PurchaseOrder purchaseOrder,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/purchaseOrder/" + purchaseOrder.getId();
        }
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_COMPLETED);
        if (purchaseOrder.getId() == null) {
            if (purchaseOrderService.lastPurchaseOrder() == null) {
                //need to generate new one
                purchaseOrder.setCode("PO" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
            } else {

                //if there is customer in db need to get that customer's code and increase its value
                String previousCode = purchaseOrderService.lastPurchaseOrder().getCode().substring(2);
                purchaseOrder.setCode("PO" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
            }
        }
        List<PurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();
        for (PurchaseOrderItem purchaseOrderItem : purchaseOrder.getPurchaseOrderItems()) {
            if (purchaseOrderItem.getItem() != null) {
                purchaseOrderItem.setPurchaseOrder(purchaseOrder);
                purchaseOrderItemList.add(purchaseOrderItem);
            }
        }
        purchaseOrder.setPurchaseOrderItems(purchaseOrderItemList);
        PurchaseOrder purchaseOrderSaved = purchaseOrderService.persist(purchaseOrder);

        if (purchaseOrderSaved.getSupplier().getEmail() != null) {
            StringBuilder message = new StringBuilder("Item Name\t\t\t\t\tQuantity\t\t\tItem Price\t\t\tTotal(Rs)\n");
            for (int i = 0; i < purchaseOrder.getPurchaseOrderItems().size(); i++) {
              Item item = itemService.findById(purchaseOrder.getPurchaseOrderItems().get(i).getItem().getId());
              PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.findById(purchaseOrderSaved.getPurchaseOrderItems().get(i).getId());
                message
                        .append(item.getName())
                        .append("\t\t\t\t\t")
                        .append(purchaseOrderItem.getQuantity())
                        .append("\t\t\t\t\t")
                        .append(item.getSellPrice())
                        .append("\t\t\t\t\t")
                        .append(purchaseOrderItem.getLineTotal())
                        .append("\n");
            }
            emailService.sendEmail(purchaseOrderSaved.getSupplier().getEmail(),
                    "Requesting Items According To PO Code " + purchaseOrder.getCode(), message.toString());
            if (purchaseOrderSaved.getSupplier().getContactOne() != null) {
                try {
                  String mobileNumber = purchaseOrderSaved.getSupplier().getContactOne().substring(1,10);
                    twilioMessageService.sendSMS("+94"+mobileNumber, "There is immediate PO from " +
                            "KMart Super \nPlease Check Your Email Form Further Details");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/purchaseOrder/all";
    }

    @GetMapping("/all")
    public String findAll(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.findAll()
                .stream()
                .filter(x -> x.getPurchaseOrderStatus().equals(PurchaseOrderStatus.NOT_COMPLETED))
                .collect(Collectors.toList()));
        model.addAttribute("heading", "Pending Purchase Order");
        model.addAttribute("grnStatus", true);
        return "purchaseOrder/purchaseOrder";
    }

    @GetMapping("view/{id}")
    public String viewPurchaseOrderDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("purchaseOrderDetail", purchaseOrderService.findById(id));
        return "purchaseOrder/purchaseOrder-detail";
    }

    @GetMapping("remove/{id}")
    public String deletePurchaseOrderDetail(@PathVariable Integer id) {
//        PurchaseOrder purchaseOrder = purchaseOrderService.findById(id);
//        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_PROCEED);
//        purchaseOrderService.persist(purchaseOrder);
//        return "redirect:/purchaseOrder/all";
        purchaseOrderService.delete(id);
        return "redirect:/purchaseOrder";
    }

    @GetMapping("/supplier/{id}")
    public String addPriceToSupplierItem(@PathVariable int id, Model model) {
        Supplier supplier = supplierService.findById(id);
        // supplier.setSupplierItems(purchaseOrders);
        model.addAttribute("supplierDetail", supplier);
        model.addAttribute("supplierDetailShow", false);
        model.addAttribute("purchaseOrderItemEdit", false);
        model.addAttribute("purchaseOrder", new PurchaseOrder());
        model.addAttribute("purchaseOrderPriorities", PurchaseOrderPriority.values());
        //send all active item belongs to supplier
        model.addAttribute("items", commonService.activeItemsFromSupplier(supplier));
        Object[] argument = {"", ""};
        model.addAttribute("purchaseOrderItemUrl", MvcUriComponentsBuilder
                .fromMethodName(SupplierItemController.class, "purchaseOrderSupplierItem", argument)
                .build()
                .toString());

        return "purchaseOrder/addPurchaseOrder";
    }

}

