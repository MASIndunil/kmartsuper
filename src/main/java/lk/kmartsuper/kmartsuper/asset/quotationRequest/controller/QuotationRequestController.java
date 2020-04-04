package lk.kmartsuper.kmartsuper.asset.quotationRequest.controller;


import lk.kmartsuper.kmartsuper.asset.item.service.ItemService;
import lk.kmartsuper.kmartsuper.asset.quotationRequest.entity.QuotationRequest;
import lk.kmartsuper.kmartsuper.asset.quotationRequest.service.QuotationRequestService;
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
@RequestMapping("/qr")
public class QuotationRequestController implements AbstractController<QuotationRequest, Integer> {
    private final QuotationRequestService quotationRequestService;
    private final ItemService itemService;

    @Autowired
    public QuotationRequestController(QuotationRequestService quotationRequestService, ItemService itemService) {
        this.quotationRequestService = quotationRequestService;
        this.itemService = itemService;
    }

    private String commonThings(Model model, QuotationRequest quotationRequest, Boolean addState) {
        model.addAttribute("quotationRequest", quotationRequest);
        model.addAttribute("addStatus", addState);
        return "quotationRequest/addqr";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("quotationRequests", quotationRequestService.findAll());
        return "quotationRequest/qr";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThings(model, new QuotationRequest(), true);
    }

    @PostMapping( value = {"/save", "/update"} )
    public String persist(QuotationRequest quotationRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if ( bindingResult.hasErrors() ) {
            return commonThings(model, quotationRequest, true);
        }

        quotationRequestService.persist(quotationRequest);
        return "redirect:/qr";
    }

    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable Integer id, Model model) {
        return commonThings(model, quotationRequestService.findById(id), false);
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        quotationRequestService.delete(id);
        return "redirect:/qr";
    }

    @GetMapping( "/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("quotationRequestDetail", quotationRequestService.findById(id));
        return "quotationRequest/qr-detail";
    }
}
