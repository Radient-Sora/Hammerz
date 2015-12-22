package vapourdrive.hammerz.handlers;

import vapourdrive.hammerz.config.ConfigOptions;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolTipHandler
{
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void itemToolTipAddition(ItemTooltipEvent event)
	{
		if (ConfigOptions.AddToolInfo)
		{
			ItemStack itemstack = event.itemStack;

			if (itemstack != null)
			{
				if (itemstack.getItem() != null)
				{
					Item item = itemstack.getItem();
					if (item instanceof ItemTool)
					{
						ToolMaterial material = ((ItemTool) item).func_150913_i();
						event.toolTip.add("Material: " + material);
						event.toolTip.add("HarvestLevel: " + material.getHarvestLevel());
						event.toolTip.add("Durability: " + material.getMaxUses());
						event.toolTip.add("Efficiency: " + material.getEfficiencyOnProperMaterial());
						event.toolTip.add("Damage: " + material.getDamageVsEntity());
						event.toolTip.add("Enchantbility: " + material.getEnchantability());
						if (material.getRepairItemStack() != null)
						{
							event.toolTip.add("RepairMaterial: " + material.getRepairItemStack().getDisplayName());
						}

					}
				}
			}
		}

		return;
	}
}
