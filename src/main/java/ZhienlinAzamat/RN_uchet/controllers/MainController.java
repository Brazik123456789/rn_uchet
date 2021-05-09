package ZhienlinAzamat.RN_uchet.controllers;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.services.ActionService;
import ZhienlinAzamat.RN_uchet.services.InventoryService;
import ZhienlinAzamat.RN_uchet.utils.InventoryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    private InventoryService inventoryService;
    private ActionService actionService;

    @Autowired
    public MainController(InventoryService inventoryService, ActionService actionService) {
        this.inventoryService = inventoryService;
        this.actionService = actionService;
    }

    @GetMapping
    public String index(Model model, @RequestParam Map<String, String> requestParams, Principal principal){
        Integer pageNumber = Integer.parseInt(requestParams.getOrDefault("p", "1"));
        InventoryFilter inventoryFilter = new InventoryFilter(requestParams);

        //  Здесь вытаскиваем весь инвентарь и из него выделяем только то, что добавил текущий пользователь
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        for (Inventory inv:inventoryService.findAll(inventoryFilter.getSpec(), pageNumber).getContent()) {
            if (inv.getAddedUser() != null ){
                if (inv.getAddedUser().equals(principal.getName()))
                    inventoryList.add(inv);
            }
        }
        Page<Inventory> inventories = new PageImpl<>(inventoryList, PageRequest.of(pageNumber - 1, 20),inventoryList.size());
//        Page<Inventory> inventories = inventoryService.findAll(inventoryFilter.getSpec(), pageNumber);
        model.addAttribute("inventories", inventories);
        model.addAttribute("filterDef", inventoryFilter.getFilterDefinition().toString());

        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal){
        model.addAttribute("userName", principal.getName());
        List<Inventory> invList = new ArrayList<>();
        for (Inventory inv:inventoryService.findAll()) {
            if (inv.getAddedUser() != null )
                if (inv.getAddedUser().equals(principal.getName()))
                    invList.add(inv);
        }
        model.addAttribute("inventoryCount", invList.size());
        if (principal.getName().equals("user"))
            model.addAttribute("userEmail", "user@mail.ru");

        model.addAttribute("years", actionService.findYears());
        model.addAttribute("months", actionService.findMonths());

        return "profileView";
    }

    @GetMapping("/info{id}")
    public String getInfo(@PathVariable("id") Long inventoryId, Model model, Principal principal){
        List<Action> actions = actionService.findByInventoryId(inventoryId);
        model.addAttribute("actions", actions);
        model.addAttribute("inventory", inventoryService.findById(inventoryId));
        return "infoView";
    }

    @PostMapping("allinfo")
    public String allInfo(@RequestParam Map<String, String> params, Model model){
        List<Action> actions = actionService.findByYearAndMonth(Long.valueOf(params.get("year")),Long.valueOf(params.get("month")));
        if (actions.size() != 0){
            model.addAttribute("history", "exist");
        }
        model.addAttribute("actions", actions);
        model.addAttribute("year", params.get("year"));
        model.addAttribute("month", params.get("month"));
        return "infoAllView";
    }
}
