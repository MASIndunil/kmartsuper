package lk.kmartsuper.kmartsuper.asset.purchaseRequest.controller;


import lk.kmartsuper.kmartsuper.asset.item.service.ItemService;
import lk.kmartsuper.kmartsuper.asset.purchaseRequest.entity.PurchaseRequest;
import lk.kmartsuper.kmartsuper.asset.purchaseRequest.service.PurchaseRequestService;
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
@RequestMapping("/pr")
public class PurchaseRequestController implements AbstractController<PurchaseRequest, Integer> {
    private final PurchaseRequestService purchaseRequestService;
    private final ItemService itemService;

    @Autowired
    public PurchaseRequestController(PurchaseRequestService purchaseRequestService, ItemService itemService) {
        this.purchaseRequestService = purchaseRequestService;
        this.itemService = itemService;
    }

    private String commonThings(Model model, PurchaseRequest purchaseRequest, Boolean addState) {
        model.addAttribute("purchaseRequest", purchaseRequest);
        model.addAttribute("addStatus", addState);
        return "purchaseRequest/addpr";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("purchaseRequests", purchaseRequestService.findAll());
        return "purchaseRequest/pr";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new PurchaseRequest(), true);
    }

    @PostMapping( value = {"/save", "/update"} )
    public String persist(PurchaseRequest purchaseRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            return commonThings(model, purchaseRequest, true);
        }

        purchaseRequestService.persist(purchaseRequest);
        return "redirect:/pr";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, purchaseRequestService.findById(id), false);
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        purchaseRequestService.delete(id);
        return "redirect:/pr";
    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("purchaseRequestDetail", purchaseRequestService.findById(id));
        return "purchaseRequest/pr-detail";
    }
}
