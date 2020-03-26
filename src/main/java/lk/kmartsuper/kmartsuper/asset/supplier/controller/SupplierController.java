package lk.kmartsuper.kmartsuper.asset.supplier.controller;


import lk.kmartsuper.kmartsuper.asset.commonAsset.model.Enum.Title;
import lk.kmartsuper.kmartsuper.asset.supplier.entity.Supplier;
import lk.kmartsuper.kmartsuper.asset.supplier.service.SupplierService;
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
@RequestMapping("/supplier")
public class SupplierController implements AbstractController<Supplier, Integer> {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    private String commonThings(Model model, Supplier supplier, Boolean addState) {
        model.addAttribute("supplier", supplier);
        model.addAttribute("addStatus", addState);
        return "supplier/addSupplier";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("suppliers", supplierService.findAll());
        return "supplier/supplier";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new Supplier(), true);
    }

    @PostMapping( value = {"/add", "/update"} )
    public String persist(Supplier supplier, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            return commonThings(model, supplier, true);
        }

        supplierService.persist(supplier);
        return "redirect:/supplier";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, supplierService.findById(id), false);
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        supplierService.delete(id);
        return "redirect:/supplier";
    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("supplierDetail", supplierService.findById(id));
        return "supplier/supplier-detail";
    }
}
