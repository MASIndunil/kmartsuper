package lk.kmart_super.asset.item.controller;

import lk.kmart_super.asset.category.controller.CategoryRestController;
import lk.kmart_super.asset.common_asset.model.enums.LiveDead;
import lk.kmart_super.asset.item.entity.Item;
import lk.kmart_super.asset.item.entity.enums.ItemStatus;
import lk.kmart_super.asset.item.entity.enums.MainCategory;
import lk.kmart_super.asset.ledger.entity.Ledger;
import lk.kmart_super.asset.purchase_order.entity.PurchaseOrder;
import lk.kmart_super.asset.purchase_order.entity.enums.PurchaseOrderStatus;
import lk.kmart_super.asset.purchase_order_item.entity.PurchaseOrderItem;
import lk.kmart_super.util.interfaces.AbstractController;
import lk.kmart_super.util.service.MakeAutoGenerateNumberService;
import lk.kmart_super.asset.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/item" )
public class ItemController implements AbstractController< Item, Integer > {
  private final ItemService itemService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  @Autowired
  public ItemController(ItemService itemService, MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.itemService = itemService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  private String commonThings(Model model, Item item, Boolean addState) {
    model.addAttribute("statuses", ItemStatus.values());
    model.addAttribute("item", item);
    model.addAttribute("addStatus", addState);
    model.addAttribute("mainCategories", MainCategory.values());
    model.addAttribute("urlMainCategory", MvcUriComponentsBuilder
        .fromMethodName(CategoryRestController.class, "getCategoryByMainCategory", "")
        .build()
        .toString());
    return "item/addItem";
  }

  @GetMapping
  public String findAll(Model model) {
    List<Item> activeItems = new ArrayList<>(itemService.findAll());
    for (Item activeItem : activeItems) {
      List<Ledger> activeLedgers = new ArrayList<>(activeItem.getLedgers());
      List<PurchaseOrderItem> relatedPOIS = new ArrayList<>(activeItem.getPurchaseOrderItems());
      int quantity = 0;
      for (Ledger activeLedger:activeLedgers) {
        quantity += Integer.parseInt(activeLedger.getQuantity());
      }
      if (quantity > 0){
        activeItem.setItemStatus(ItemStatus.AVAILABLE);
      }else {

        for (PurchaseOrderItem relatedPOI : relatedPOIS) {
          if (relatedPOI.getPurchaseOrder().getPurchaseOrderStatus() == PurchaseOrderStatus.NOT_COMPLETED) {
            activeItem.setItemStatus(ItemStatus.ORDERED);
          } else {
            activeItem.setItemStatus(ItemStatus.UNAVAILABLE);
          }

        }

      }
      itemService.persist(activeItem);
    }
    model.addAttribute("items", activeItems);
    return "item/item";
  }


  @Override
  public String findById(Integer id, Model model) {
    return null;
  }

  @GetMapping( "/add" )
  public String addForm(Model model) {
    return commonThings(model, new Item(), true);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThings(model, item, true);
    }
    if ( item.getId() == null ) {
      //if there is not item in db
      if ( itemService.lastItem() == null ) {
        //need to generate new one
        item.setCode("ITM" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        //if there is item in db need to get that item's code and increase its value
        String previousCode = itemService.lastItem().getCode().substring(3);
        item.setCode("ITM" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
      }
      item.setItemStatus(ItemStatus.NEW);
    }

    itemService.persist(item);
    return "redirect:/item";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThings(model, itemService.findById(id), false);
  }

  @GetMapping( "/remove/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    itemService.delete(id);
    return "redirect:/item";
  }

  @GetMapping( "/{id}" )
  public String view(@PathVariable Integer id, Model model) {
    model.addAttribute("itemDetail", itemService.findById(id));
    return "item/item-detail";
  }
  @GetMapping( "/rop" )
  public String findROP(Model model) {
    List<Item> activeItems = new ArrayList<>(itemService.findAll());
    List<Item> ropItems = new ArrayList<>();
    List<Integer> ropItemsQts = new ArrayList<>();
    for (Item activeItem : activeItems) {
      List<Ledger> activeLedgers = new ArrayList<>(activeItem.getLedgers());
      int qty = 0;
      for (Ledger activeLedger : activeLedgers) {
        qty = qty + Integer.parseInt(activeLedger.getQuantity());
      }
      int rop = Integer.parseInt(activeItem.getRop());
      if (qty <= rop){
        ropItems.add(activeItem);
        ropItemsQts.add(qty);
      }
    }
    model.addAttribute("ropItems", ropItems);
    model.addAttribute("ropItemsQts", ropItemsQts);
    return "ledger/rop";
  }

}
