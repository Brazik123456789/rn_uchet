package ZhienlinAzamat.RN_uchet.controllers;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.services.ActionService;
import ZhienlinAzamat.RN_uchet.services.InventoryService;
import ZhienlinAzamat.RN_uchet.utils.InventoryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    private InventoryService inventoryService;
    private ActionService actionService;
    public static Map<String, String> users;    //  (текущий пользак, добавивший пользак)

    @Autowired
    public MainController(InventoryService inventoryService, ActionService actionService) {
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("GMT+5"));     // Проставляем часовой пояс
        this.inventoryService = inventoryService;
        this.actionService = actionService;
        users = new HashMap<>();    //  Здесь добавляем пользаков(текущий пользак, добавивший пользак), где добавивший должен быть такой же как и основной пользак цеха, а текущий мб как и основным от цеха, так и читателем от этого же цеха
        users.put("user","user");
        users.put("userReader","user");
        users.put("УНП2","УНП2");
        users.put("userEmail","user@mail.ru");
        users.put("userReaderEmail","userReader@mail.ru");
        users.put("УНП2Email","xabibullin_1977@mail.ru");
    }

    @GetMapping
    public String index(Model model, @RequestParam Map<String, String> requestParams, Principal principal){
        Integer pageNumber = Integer.parseInt(requestParams.getOrDefault("p", "1"));
        requestParams.put("addedUser",users.get(principal.getName()));
        InventoryFilter inventoryFilter = new InventoryFilter(requestParams);
        Page<Inventory> inventories = inventoryService.findAll(inventoryFilter.getSpec(), pageNumber);
        model.addAttribute("inventories", inventories);
        model.addAttribute("filterDef", inventoryFilter.getFilterDefinition().toString());

        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal){
        model.addAttribute("userName", MainController.users.get(principal.getName()));
        List<Inventory> invList = new ArrayList<>();
        for (Inventory inv:inventoryService.findAll()) {
            if (inv.getAddedUser() != null )
                if (inv.getAddedUser().equals(MainController.users.get(principal.getName())))
                    invList.add(inv);
        }
        model.addAttribute("inventoryCount", invList.size());
        model.addAttribute("userEmail", users.get(principal.getName() + "Email"));
        model.addAttribute("years", actionService.findYears());
        model.addAttribute("months", actionService.findMonths());

        return "profileView";
    }

    @GetMapping("/info{id}")
    public String getInfo(@PathVariable("id") Long inventoryId, Model model){
        List<Action> actions = actionService.findByInventoryId(inventoryId);
        model.addAttribute("actions", actions);
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        return "infoView";
    }

    @PostMapping("allinfo")
    public String allInfo(@RequestParam Map<String, String> params, Model model, Principal principal){
        List<Action> actions = actionService.findByYearAndMonth(Long.valueOf(params.get("year")),Long.valueOf(params.get("month")), users.get(principal.getName()));
        if (actions.size() != 0){
            model.addAttribute("history", "exist");
        }
        model.addAttribute("actions", actions);
        model.addAttribute("year", params.get("year"));
        model.addAttribute("month", params.get("month"));
        return "infoAllView";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "accessDeniedView";
    }
}
