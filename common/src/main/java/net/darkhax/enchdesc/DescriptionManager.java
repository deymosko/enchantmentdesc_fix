package net.darkhax.enchdesc;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DescriptionManager {

    private final ConfigSchema config;
    private final Map<Enchantment, MutableComponent> descriptions = new ConcurrentHashMap<>();

    public DescriptionManager(ConfigSchema config) {
        this.config = config;
    }

    public MutableComponent get(Enchantment ench) {

        return descriptions.computeIfAbsent(ench, e -> {

            String descriptionKey = e.getDescriptionId() + ".desc";

            if (!I18n.exists(descriptionKey) && I18n.exists(e.getDescriptionId() + ".description")) {

                descriptionKey = e.getDescriptionId() + ".description";
            }

            return Component.translatable(descriptionKey).withStyle(config.style);
        });
    }
}