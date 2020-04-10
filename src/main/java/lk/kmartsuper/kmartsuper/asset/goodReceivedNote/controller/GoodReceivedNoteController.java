package lk.kmartsuper.kmartsuper.asset.goodReceivedNote.controller;

import lk.kmartsuper.kmartsuper.asset.goodReceivedNote.service.GoodReceivedNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goodReceivedNote")
public class GoodReceivedNoteController {
    private final GoodReceivedNoteService goodReceivedNoteService;
@Autowired
    public GoodReceivedNoteController(GoodReceivedNoteService goodReceivedNoteService) {
        this.goodReceivedNoteService = goodReceivedNoteService;
    }
}
