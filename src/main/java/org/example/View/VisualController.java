package org.example.View;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class VisualController {

    @GetMapping("/visualize")
    public String visualize() {
        return "visualize";
    }

    @GetMapping("/visualizeWithModel")
    public String visualizeWithModel(Model model) {

        model.addAttribute("attribute", "value");
        return "visualize";
    }

}
