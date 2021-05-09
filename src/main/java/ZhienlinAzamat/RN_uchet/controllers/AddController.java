package ZhienlinAzamat.RN_uchet.controllers;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.services.ActionService;
import ZhienlinAzamat.RN_uchet.services.InventoryService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AddController {
    private InventoryService inventoryService;
    private ActionService actionService;

    @Autowired
    public AddController(InventoryService inventoryService, ActionService actionService) {
        this.inventoryService = inventoryService;
        this.actionService = actionService;
    }

    @GetMapping("/add")
    public String getAddView(){
        return "addView";
    }

    @PostMapping("/add")
    public String addInventory(@ModelAttribute Inventory inventory, @RequestParam String comment, Principal principal){
        inventory.setAddedUser(principal.getName());
        inventoryService.saveOrUpdate(inventory);
        Action newAction = new Action(inventory,"Добавил данный инвентарь", inventory.getOstatok(), principal.getName(), principal.getName(), inventory.getOstatok(), new Date(), comment);
        actionService.saveOrUpdate(newAction);
        List<Action> actions = new ArrayList<Action>();
        actions.add(newAction);
        inventory.setActions(actions);
        inventoryService.saveOrUpdate(inventory);
        return "redirect:/";
    }
}
