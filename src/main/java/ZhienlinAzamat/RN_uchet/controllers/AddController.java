package ZhienlinAzamat.RN_uchet.controllers;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.services.ActionService;
import ZhienlinAzamat.RN_uchet.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@Controller
public class AddController {
    private InventoryService inventoryService;
    private ActionService actionService;
//    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//    Date date = isoFormat.parse("2010-05-23T09:01:02");

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
        inventory.setAddedUser(MainController.users.get(principal.getName()));
        inventoryService.saveOrUpdate(inventory);
        Action newAction = new Action(inventory,"Добавил данный инвентарь", inventory.getOstatok(), MainController.users.get(principal.getName()), MainController.users.get(principal.getName()), inventory.getOstatok(), new Date(), comment);
        actionService.saveOrUpdate(newAction);
        List<Action> actions = new ArrayList<Action>();
        actions.add(newAction);
        inventory.setActions(actions);
        inventoryService.saveOrUpdate(inventory);
        return "redirect:/";
    }
}
