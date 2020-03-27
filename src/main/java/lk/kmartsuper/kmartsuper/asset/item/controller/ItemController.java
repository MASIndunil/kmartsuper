package lk.kmartsuper.kmartsuper.asset.item.controller;


import lk.kmartsuper.kmartsuper.asset.item.entity.Enum.Category;
import lk.kmartsuper.kmartsuper.asset.item.entity.Enum.Status;
import lk.kmartsuper.kmartsuper.asset.item.entity.Item;
import lk.kmartsuper.kmartsuper.asset.item.service.ItemService;
import lk.kmartsuper.kmartsuper.util.interfaces.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/item")
public class ItemController implements AbstractController<Item, Integer> {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    private String commonThings(Model model, Item item, Boolean addState) {
        model.addAttribute("category", Category.values());
        model.addAttribute("status", Status.values());
        model.addAttribute("item", item);
        model.addAttribute("addStatus", addState);
        return "item/addItem";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "item/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new Item(), true);
    }

    @PostMapping( value = {"/add", "/update"} )
    public String persist(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            return commonThings(model, item, true);
        }

        itemService.persist(item);
        return "redirect:/item";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, itemService.findById(id), false);
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        itemService.delete(id);
        return "redirect:/item";
    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("itemDetail", itemService.findById(id));
        return "item/item-detail";
    }
}
