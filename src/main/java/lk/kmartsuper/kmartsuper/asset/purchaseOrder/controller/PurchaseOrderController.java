package lk.kmartsuper.kmartsuper.asset.purchaseOrder.controller;


import lk.kmartsuper.kmartsuper.asset.item.service.ItemService;
import lk.kmartsuper.kmartsuper.asset.purchaseOrder.entity.Enum.PurchaseOrderStatus;
import lk.kmartsuper.kmartsuper.asset.purchaseOrder.entity.PurchaseOrder;
import lk.kmartsuper.kmartsuper.asset.purchaseOrder.service.PurchaseOrderService;
import lk.kmartsuper.kmartsuper.asset.supplier.entity.Supplier;
import lk.kmartsuper.kmartsuper.asset.supplier.service.SupplierService;
import lk.kmartsuper.kmartsuper.util.service.MakeAutoGenerateNumberService;
import org.hibernate.type.StringNVarcharType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
    private final ItemService itemService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, SupplierService supplierService, MakeAutoGenerateNumberService makeAutoGenerateNumberService, ItemService itemService) {
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
        this.itemService = itemService;
    }

    @GetMapping
    public String addForm(Model model) {
        model.addAttribute("purchaseOrder", new PurchaseOrder());
        model.addAttribute("searchAreaShow", true);
        return "purchaseOrder/addPurchaseOrder";
    }

    @PostMapping("/find")
    public String search(@Valid @ModelAttribute Supplier supplier, Model model) {
        if (supplier.getContactOne() != null) {
            supplier.setContactOne(makeAutoGenerateNumberService.phoneNumberLengthValidator(supplier.getContactOne()));
            supplier.setContactTwo(makeAutoGenerateNumberService.phoneNumberLengthValidator(supplier.getContactTwo()));
        }
        List<Supplier> suppliers = supplierService.search(supplier);
        model.addAttribute("searchAreaShow", false);
        if (suppliers.size() == 1) {
            model.addAttribute("supplierDetail", suppliers.get(0));
            model.addAttribute("supplierDetailShow", false);
            return "purchaseOrder/addPurchaseOrder";
        }
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierDetailShow", true);
        return "purchaseOrder/addPurchaseOrder";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("searchAreaShow", false);
        model.addAttribute("supplierDetail", supplierService.findById(id));
        model.addAttribute("supplierDetailShow", false);
        return "purchaseOrder/addPurchaseOrder";
    }

    @PostMapping
    public String supplierItemPersist(@Valid @ModelAttribute PurchaseOrder purchaseOrder, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/supplierItem/" + purchaseOrder.getId();
        }
        purchaseOrder.getPurchaseOrderItems().forEach(purchaseOrderItem -> purchaseOrderItem.setPurchaseOrder(purchaseOrder));
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_COMPLETED);
        purchaseOrderService.persist(purchaseOrder);
        return "redirect:/purchaseOrder/all";
    }

    @GetMapping("/all")
    public String findAll(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.findAll());
        return "purchaseOrder/purchaseOrder";
    }

    @GetMapping("view/{id}")
    public String viewPurchaseOrderDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("purchaseOrder-details", purchaseOrderService.findById(id));
        return "purchaseOrder/purchaseOrder-detail";
    }

    @GetMapping("delete/{id}")
    public String deletePurchaseOrderDetail(@PathVariable Integer id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.findById(id);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.NOT_PROCEED);
        purchaseOrderService.persist(purchaseOrder);
        //model.addAttribute("purchaseOrder-details", purchaseOrderService.findById(id));
        return "redirect:/purchaseOrder/all";
    }

}
