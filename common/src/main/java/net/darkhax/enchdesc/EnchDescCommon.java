package net.darkhax.enchdesc;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class EnchDescCommon {

    private final ConfigSchema config;
    private final DescriptionManager descriptions;

    public EnchDescCommon(Path configPath) {

        ItemStack
        this.config = ConfigSchema.load(configPath.resolve(Constants.MOD_ID + ".json").toFile());
        this.descriptions = new DescriptionManager(this.config);
        Services.EVENTS.addItemTooltipListener(this::onItemTooltip);
    }

    private void onItemTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (this.config.enableMod && !stack.isEmpty() && stack.hasTag()) {

            if ((!config.onlyDisplayOnBooks && stack.isEnchanted()) || stack.getItem() instanceof EnchantedBookItem) {

                if (!config.onlyDisplayInEnchantingTable || Minecraft.getInstance().screen instanceof EnchantmentScreen) {

                    final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

                    if (!enchantments.isEmpty()) {

                        if (!config.requireKeybindPress || Screen.hasShiftDown()) {

                            for (Enchantment enchantment : enchantments.keySet()) {

                                final Component fullName = enchantment.getFullname(enchantments.get(enchantment));

                                for (Component line : tooltip) {

                                    if (line.equals(fullName)) {

                                        Component descriptionText = descriptions.get(enchantment);

                                        if (config.indentSize > 0) {
                                            descriptionText = Component.literal(StringUtils.repeat(' ', config.indentSize)).append(descriptionText);
                                        }

                                        tooltip.add(tooltip.indexOf(line) + 1, descriptionText);
                                        break;
                                    }
                                }
                            }
                        }

                        else {

                            tooltip.add(Component.translatable("enchdesc.activate.message").withStyle(ChatFormatting.DARK_GRAY));
                        }
                    }
                }
            }
        }
    }
}