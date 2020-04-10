package lk.kmartsuper.kmartsuper.asset.supplier.controller;

import lk.kmartsuper.kmartsuper.asset.item.category.service.CategoryService;
import lk.kmartsuper.kmartsuper.asset.item.service.ItemService;
import lk.kmartsuper.kmartsuper.asset.supplier.entity.Supplier;
import lk.kmartsuper.kmartsuper.asset.supplier.service.SupplierService;
import lk.kmartsuper.kmartsuper.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/supplierItem")
public class SupplierItemController {
    private final ItemService itemService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

    @Autowired
    public SupplierItemController(ItemService itemService, SupplierService supplierService, CategoryService categoryService, MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
        this.itemService = itemService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    }

    @GetMapping
    public String addForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("searchAreaShow", true);
        return "supplier/addSupplierItem";
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
            return "supplier/addSupplierItem";
        }
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierDetailShow", true);
        return "supplier/addSupplierItem";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("searchAreaShow", false);
        model.addAttribute("supplierDetail", supplierService.findById(id));
        model.addAttribute("supplierDetailShow", false);
        model.addAttribute("items", itemService.findAll());
        return "supplier/addSupplierItem";
    }

    @PostMapping
    public String supplierItemPersist(@Valid @ModelAttribute Supplier supplier, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/supplierItem/" + supplier.getId();
        }
        redirectAttributes.addFlashAttribute("items", supplierService.persist(supplier).getItems());
        return "redirect:/supplier";
    }

}
