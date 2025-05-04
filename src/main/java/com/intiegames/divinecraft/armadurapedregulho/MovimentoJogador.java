package com.intiegames.divinecraft.armadurapedregulho;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


import java.util.UUID;

public class MovimentoJogador implements Listener {

    private static final UUID MODIFICADOR_PENALIDADE_PESO_UUID = UUID.nameUUIDFromBytes("ArmaduraDePedregulho:penalidade_peso".getBytes());
    private final JavaPlugin plugin;

    // Pesos das peças da armadura
    private final double pesoCapacete;
    private final double pesoPeitoral;
    private final double pesoCalcas;
    private final double pesoBotas;

    // Configuração para afundar quando sobrecarregado
    private final boolean sinkWhenOverloaded;
    private final double sinkingSpeed;

    public MovimentoJogador(JavaPlugin plugin) {
        this.plugin = plugin;
        // Carregar os pesos do arquivo de configuração
        pesoCapacete = plugin.getConfig().getDouble("weights.helmet", 0.20);
        pesoPeitoral = plugin.getConfig().getDouble("weights.chestplate", 0.40);
        pesoCalcas = plugin.getConfig().getDouble("weights.leggings", 0.25);
        pesoBotas = plugin.getConfig().getDouble("weights.boots", 0.15);

        // Carregar a configuração de afundar quando sobrecarregado
        sinkWhenOverloaded = plugin.getConfig().getBoolean("sink_when_overloaded", true);
        sinkingSpeed = plugin.getConfig().getDouble("sinking_speed", 0.1);
    }

    @EventHandler
    public void aoMover(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Verificar se o jogador está na água
        if (player.isInWater()) {
            double pesoTotal = 0.0;

            // Verificar o capacete
            ItemStack capacete = player.getInventory().getHelmet();
            if (capacete != null && isArmaduraDePedregulho(capacete)) {
                pesoTotal += pesoCapacete;
            }

            // Verificar o peitoral
            ItemStack peitoral = player.getInventory().getChestplate();
            if (peitoral != null && isArmaduraDePedregulho(peitoral)) {
                pesoTotal += pesoPeitoral;
            }

            // Verificar as calças
            ItemStack calcas = player.getInventory().getLeggings();
            if (calcas != null && isArmaduraDePedregulho(calcas)) {
                pesoTotal += pesoCalcas;
            }

            // Verificar as botas
            ItemStack botas = player.getInventory().getBoots();
            if (botas != null && isArmaduraDePedregulho(botas)) {
                pesoTotal += pesoBotas;
            }

            if (pesoTotal > 0.0) {
                if (pesoTotal >= 1.0) {
                    // Peso total é 100% ou mais - jogador afunda na água
                    if (sinkWhenOverloaded) {
                        // Verificar se o jogador está totalmente submerso
                        if (player.getEyeLocation().getBlock().isLiquid()) {
                            // Fazer o jogador afundar
                            player.setVelocity(player.getVelocity().add(new Vector(0, -sinkingSpeed, 0)));
                        }
                    } else {
                        // Jogador não pode se mover
                        event.setCancelled(true);
                    }
                    player.sendTitle("", ChatColor.RED + "Você está muito pesado para nadar!", 0, 40, 10);
                } else {
                    // Aplicar penalidade proporcional
                    AttributeInstance atributoVelocidade = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                    if (atributoVelocidade != null) {
                        // Remover modificador existente
                        removerModificadorVelocidade(atributoVelocidade);

                        // Criar e adicionar o modificador
                        AttributeModifier modificador = new AttributeModifier(
                                MODIFICADOR_PENALIDADE_PESO_UUID,
                                "penalidade_peso",
                                -atributoVelocidade.getBaseValue() * pesoTotal,
                                AttributeModifier.Operation.ADD_NUMBER
                        );
                        atributoVelocidade.addModifier(modificador);
                    }
                }
            } else {
                // Remover penalidades se o jogador não estiver usando armadura de pedregulho
                restaurarVelocidade(player);
            }
        } else {
            // Restaurar a velocidade ao sair da água
            restaurarVelocidade(player);
        }
    }

    private void removerModificadorVelocidade(AttributeInstance atributoVelocidade) {
        AttributeModifier modificadorExistente = null;

        for (AttributeModifier modifier : atributoVelocidade.getModifiers()) {
            if (modifier.getUniqueId().equals(MODIFICADOR_PENALIDADE_PESO_UUID)) {
                modificadorExistente = modifier;
                break;
            }
        }

        if (modificadorExistente != null) {
            atributoVelocidade.removeModifier(modificadorExistente);
        }
    }

    private void restaurarVelocidade(Player player) {
        AttributeInstance atributoVelocidade = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (atributoVelocidade != null) {
            removerModificadorVelocidade(atributoVelocidade);
        }
    }

    private boolean isArmaduraDePedregulho(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey chave = new NamespacedKey(plugin, "armadura_pedregulho");
        return meta.getPersistentDataContainer().has(chave, PersistentDataType.BYTE);
    }
}
