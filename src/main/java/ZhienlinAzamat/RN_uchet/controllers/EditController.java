package ZhienlinAzamat.RN_uchet.controllers;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.Entities.Rent;
import ZhienlinAzamat.RN_uchet.services.ActionService;
import ZhienlinAzamat.RN_uchet.services.InventoryService;
import ZhienlinAzamat.RN_uchet.services.RentService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class EditController {
    private InventoryService inventoryService;
    private ActionService actionService;
    private RentService rentService;

    @Autowired
    public EditController(InventoryService inventoryService, ActionService actionService, RentService rentService) {
        this.inventoryService = inventoryService;
        this.actionService = actionService;
        this.rentService = rentService;
    }

    @GetMapping("/edit{id}")
    public String getEditView(@PathVariable("id") Long inventoryId, Model model){
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        List<Rent> rents = rentService.findByInventoryId(inventoryId);
        Collections.reverse(rents);
        model.addAttribute("rents", rents);

        return "editView";
    }

    @PostMapping("/edit{id}")
    public String editInventory(@PathVariable("id") Long inventoryId, @RequestParam Map<String, String> params, Principal principal){
        Inventory inventory = inventoryService.findById(inventoryId);

        if (params.containsKey("takeCheck")) {
            Action newAction = new Action(inventory,"Взял", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), params.get("recipient"), Precision.round(inventoryService.findById(inventoryId).getOstatok()-Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
            actionService.saveOrUpdate(newAction);
            Rent newRent = new Rent();
            newRent.setInventory(inventory);
            newRent.setRecipient(newAction.getRecipient());
            newRent.setCount(Precision.round(newAction.getCount(), 3));
            rentService.saveOrUpdate(newRent);
            inventory.getRents().add(newRent);
            inventory.setOstatok(Precision.round(inventoryService.findById(inventoryId).getOstatok()-newAction.getCount(),3));
            inventory.getActions().add(newAction);
        } else {
            Action newAction = new Action(inventory,"Взял безвозвратно", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), params.get("recipient"), Precision.round(inventoryService.findById(inventoryId).getOstatok()-Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
            actionService.saveOrUpdate(newAction);
            inventory.setOstatok(Precision.round(inventoryService.findById(inventoryId).getOstatok()-newAction.getCount(),3));
            inventory.getActions().add(newAction);
        }
        inventoryService.saveOrUpdate(inventory);

        return "redirect:/edit"+inventoryId;
    }

    @PostMapping("/rentEdit{id}")
    public String editAction(@PathVariable("id") Long rentId, @RequestParam Map<String,String> params, Principal principal, Model model){
        Rent rent = rentService.findById(rentId);
        Inventory inventory = rent.getInventory();
        if (params.get("action").equals("takeAll")){
            Action newAction = new Action(inventory,"Вернул", rent.getCount(), MainController.users.get(principal.getName()), rent.getRecipient(), Precision.round(inventory.getOstatok() + rent.getCount(),3), new Date(),"");
            inventory.setOstatok(Precision.round(newAction.getGivingUserOstatok(),3));
            inventoryService.saveOrUpdate(inventory);
            rentService.delete(rentId);
            actionService.saveOrUpdate(newAction);
        } else if (params.get("action").equals("takeShare")){
            if (params.get("takeShareView").equals("true")){    //  тут прописать остаток на складе другой
                Action newAction = new Action(inventory,"Вернул", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), rent.getRecipient(), Precision.round(inventory.getOstatok() + Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
                rent.setCount(Precision.round(rent.getCount() - Double.parseDouble(params.get("count")),3));
                inventory.setOstatok(Precision.round(newAction.getGivingUserOstatok(),3));
                inventoryService.saveOrUpdate(inventory);
                rentService.saveOrUpdate(rent);
                actionService.saveOrUpdate(newAction);
            } else if (params.get("takeShareView").equals("false")){
                model.addAttribute("rent",rent);
                model.addAttribute("inventory",inventory);
                return "takeShareView";
            }
        } else if (params.get("action").equals("giveShare")){
            if (params.get("giveShareView").equals("true")){    //  тут прописать остаток на складе другой
                Action newAction = new Action(inventory,"Взял", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), rent.getRecipient(), Precision.round(inventory.getOstatok() - Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
                rent.setCount(Precision.round(rent.getCount() + Double.parseDouble(params.get("count")),3));
                inventory.setOstatok(Precision.round(newAction.getGivingUserOstatok(),3));
                inventoryService.saveOrUpdate(inventory);
                rentService.saveOrUpdate(rent);
                actionService.saveOrUpdate(newAction);
            } else if (params.get("giveShareView").equals("false")){
                model.addAttribute("rent",rent);
                model.addAttribute("inventory",inventory);
                return "giveShareView";
            }
        }

        return "redirect:/edit" + inventory.getId();
    }

    @GetMapping("/editostatokadd{id}")
    public String editOstatokAdd(@PathVariable("id") Long inventoryId, Model model){
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        return "editOstatokAddView";
    }

    @GetMapping("/editostatokdelete{id}")
    public String editOstatokDelete(@PathVariable("id") Long inventoryId, Model model){
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        return "editOstatokDeleteView";
    }

    @PostMapping("/editostatok{id}")
    public String editOstatokPost(@PathVariable("id") Long inventoryId, @RequestParam Map<String, String> params, Principal principal){
        Inventory inventory = inventoryService.findById(inventoryId);
        if (params.get("action").equals("+")){
            Action newAction = new Action(inventory,"Вручную увеличил остаток", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), MainController.users.get(principal.getName()), Precision.round(inventory.getOstatok()+Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
            inventory.setOstatok(Precision.round(inventory.getOstatok()+newAction.getCount(),3));
            inventory.getActions().add(newAction);
            actionService.saveOrUpdate(newAction);
            inventoryService.saveOrUpdate(inventory);
        } else if (params.get("action").equals("-")){
            Action newAction = new Action(inventory,"Вручную уменьшил остаток", Precision.round(Double.parseDouble(params.get("count")),3), MainController.users.get(principal.getName()), MainController.users.get(principal.getName()), Precision.round(inventory.getOstatok()-Double.parseDouble(params.get("count")),3), new Date(), params.get("comment"));
            inventory.setOstatok(Precision.round(inventory.getOstatok()-newAction.getCount(),3));
            inventory.getActions().add(newAction);
            actionService.saveOrUpdate(newAction);
            inventoryService.saveOrUpdate(inventory);
        }

        return "redirect:/edit"+inventoryId;
    }

    @GetMapping("/deleteInventory{id}")
    public String deleteInventoryGet(@PathVariable("id") Long inventoryId, Model model){
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        return "deleteView";
    }

    @PostMapping("/deleteInventory{id}")
    public String deleteInventoryPost(@PathVariable("id") Long inventoryId, @RequestParam String comment, Model model, Principal principal){
        Inventory inventory = inventoryService.findById(inventoryId);
        Action newAction = new Action(inventory,"Удалил данный инвентарь", Precision.round(inventory.getOstatok(),3), MainController.users.get(principal.getName()), MainController.users.get(principal.getName()), 0, new Date(), comment);
        actionService.saveOrUpdate(newAction);
        inventoryService.delete(inventoryId);
        return "redirect:/";
    }
}