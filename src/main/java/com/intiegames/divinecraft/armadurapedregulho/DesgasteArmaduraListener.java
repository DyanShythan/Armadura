package com.intiegames.divinecraft.armadurapedregulho;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DesgasteArmaduraListener implements Listener {

    private final ArmaduraPedregulho plugin;

    public DesgasteArmaduraListener(ArmaduraPedregulho plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Aplica desgaste adicional a cada peça de armadura de pedregulho
            for (ItemStack item : player.getInventory().getArmorContents()) {
                if (isArmaduraDePedregulho(item)) {
                    int currentDurability = item.getDurability();
                    item.setDurability((short) (currentDurability + 2)); // Aumenta o desgaste
                }
            }
        }
    }

    private boolean isArmaduraDePedregulho(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey chave = new NamespacedKey(plugin, "armadura_pedregulho");
        return meta.getPersistentDataContainer().has(chave, PersistentDataType.BYTE);
    }
}
