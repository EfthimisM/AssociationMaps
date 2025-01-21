package org.example.View;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class VisualController {


    // Method mapped to /visualize
    @GetMapping("/visualize")
    public String visualize() {


        // Logic for visualization without Model
        return "visualize";
    }

    // Method mapped to /visualizeWithModel
    @GetMapping("/visualizeWithModel")
    public String visualizeWithModel(Model model) {
        // Logic for visualization with Model
        model.addAttribute("attribute", "value");
        return "visualize";
    }

}
