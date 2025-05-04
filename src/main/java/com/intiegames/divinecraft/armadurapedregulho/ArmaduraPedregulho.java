package com.intiegames.divinecraft.armadurapedregulho;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ArmaduraPedregulho extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin ArmaduraDePedregulho ativado com sucesso!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-----------------------------");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Plugin ArmaduraDePedregulho ativado!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Desenvolvido por DyanShythan");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "-----------------------------");
        criarCapaceteDePedregulho();
        criarPeitoralDePedregulho();
        criarCalcasDePedregulho();
        criarBotasDePedregulho();

        // Registrar o listener de movimento e desgaste
        getServer().getPluginManager().registerEvents(new MovimentoJogador(this), this);
        getServer().getPluginManager().registerEvents(new DesgasteArmaduraListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin ArmaduraDePedregulho desativado.");
    }

    private ItemStack criarArmaduraDePedregulho(Material material, String nome, EquipmentSlot slot, String recipeKey, String[] shape) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Definir o nome e a descrição
        meta.setDisplayName(ChatColor.GRAY + nome);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Uma peça resistente feita de pedregulho.");
        meta.setLore(lore);

        // Definir o encantamento de proteção nível 1
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

        // Marcar o item como armadura de pedregulho
        NamespacedKey chave = new NamespacedKey(this, "armadura_pedregulho");
        meta.getPersistentDataContainer().set(chave, PersistentDataType.BYTE, (byte)1);

        // Definir a cor escura, simulando a aparência de pedregulho, se for armadura de couro
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            leatherMeta.setColor(Color.fromRGB(96, 96, 96)); // Cor cinza médio para aparência de pedra
            meta = leatherMeta;
        }

        item.setItemMeta(meta);

        // Criar a receita de crafting
        ShapedRecipe receita = new ShapedRecipe(new NamespacedKey(this, recipeKey), item);
        receita.shape(shape);
        receita.setIngredient('P', Material.COBBLESTONE);

        // Registrar a receita
        Bukkit.addRecipe(receita);
        getLogger().info("Receita da " + nome + " registrada com sucesso!");

        return item;
    }

    private void criarCapaceteDePedregulho() {
        String nome = "Capacete de Pedregulho";
        criarArmaduraDePedregulho(
                Material.LEATHER_HELMET,
                nome,
                EquipmentSlot.HEAD,
                "capacete_de_pedregulho",
                new String[]{"PPP", "P P"}
        );
    }

    private void criarPeitoralDePedregulho() {
        String nome = "Peitoral de Pedregulho";
        criarArmaduraDePedregulho(
                Material.LEATHER_CHESTPLATE,
                nome,
                EquipmentSlot.CHEST,
                "peitoral_de_pedregulho",
                new String[]{"P P", "PPP", "PPP"}
        );
    }

    private void criarCalcasDePedregulho() {
        String nome = "Calças de Pedregulho";
        criarArmaduraDePedregulho(
                Material.LEATHER_LEGGINGS,
                nome,
                EquipmentSlot.LEGS,
                "calcas_de_pedregulho",
                new String[]{"PPP", "P P", "P P"}
        );
    }

    private void criarBotasDePedregulho() {
        String nome = "Botas de Pedregulho";
        criarArmaduraDePedregulho(
                Material.LEATHER_BOOTS,
                nome,
                EquipmentSlot.FEET,
                "botas_de_pedregulho",
                new String[]{"P P", "P P"}
        );
    }
}
