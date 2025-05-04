package com.intiegames.divinecraft.armadurapedregulho;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ArmaduraCobre {

    private final JavaPlugin plugin;

    public ArmaduraCobre(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registrarArmaduras() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Registrando Armadura de Cobre...");
        criarCapacete();
        criarPeitoral();
        criarCalcas();
        criarBotas();
    }

    private void criarArmadura(String nome, Material material, EquipmentSlot slot, Color cor, String recipeKey, String[] shape) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + nome);
        meta.setColor(cor);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        item.setItemMeta(meta);

        ShapedRecipe receita = new ShapedRecipe(new NamespacedKey(plugin, recipeKey), item);
        receita.shape(shape);
        receita.setIngredient('C', Material.COPPER_INGOT);

        Bukkit.addRecipe(receita);
    }

    private void criarCapacete() {
        criarArmadura("Capacete de Cobre", Material.LEATHER_HELMET, EquipmentSlot.HEAD, Color.fromRGB(184, 115, 51), "capacete_cobre", new String[]{"CCC", "C C"});
    }

    private void criarPeitoral() {
        criarArmadura("Peitoral de Cobre", Material.LEATHER_CHESTPLATE, EquipmentSlot.CHEST, Color.fromRGB(184, 115, 51), "peitoral_cobre", new String[]{"C C", "CCC", "CCC"});
    }

    private void criarCalcas() {
        criarArmadura("Calças de Cobre", Material.LEATHER_LEGGINGS, EquipmentSlot.LEGS, Color.fromRGB(184, 115, 51), "calcas_cobre", new String[]{"CCC", "C C", "C C"});
    }

    private void criarBotas() {
        criarArmadura("Botas de Cobre", Material.LEATHER_BOOTS, EquipmentSlot.FEET, Color.fromRGB(184, 115, 51), "botas_cobre", new String[]{"C C", "C C"});
    }
}
