package sora.hammerx.items.hammer;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import teamroots.embers.util.EmberInventoryUtil;
import sora.hammerx.config.ConfigOptions;
import sora.hammerx.handlers.UpgradeManager;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.Random;

import static sora.hammerx.items.hammer.EnergyHandler.getEmpoweredment;

public class DamageHandler
{
	public static final int MANA_PER_DAMAGE = 60;

	public static boolean handleDamage(boolean force, IBlockState state, ItemStack stack, EntityLivingBase entityLiving)
	{
		return requestDamage(force, state, stack, entityLiving, 1);
	}
	
	public static boolean requestDamage(boolean force, IBlockState state, ItemStack stack, EntityLivingBase entityLiving, int damage)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;
			if(player.capabilities.isCreativeMode)
			{
				return true;
			}
		}
		if(!HammerInfoHandler.getTakesDamage(stack))
		{
			return true;
		}
		if(HammerInfoHandler.getUsesMana(stack))
		{
			return handleManaDamage(force, stack, damage, entityLiving);
		}
		else if(HammerInfoHandler.getUsesEnergy(stack))
		{
			return handleEnergyDamage(force, state, stack, damage, entityLiving);
		}
		else if(HammerInfoHandler.getUsesEmber(stack))
		{
			return handleEmberDamage(force, stack, damage, entityLiving);
		}
		else if(HammerInfoHandler.getUsesLP(stack))
		{
			return handleLPDamage(force, stack, damage, entityLiving);
		}
		else if(HammerInfoHandler.getUsesDW(stack))
		{
			return handleDWDamage(force,stack,damage,entityLiving);
		}

		return handleRegularDamage(force, stack, damage, entityLiving);
	}

	private static boolean handleRegularDamage(boolean force, ItemStack stack, int damage, EntityLivingBase entityLiving)
	{
		if (!force && (stack.getMaxDamage() - stack.getItemDamage()) < damage)
		{
			return false;
		}
		stack.damageItem(damage, entityLiving);
		/*if (stack.stackSize == 0)
		{
			return false;
		}*/
		return true;
	}

	private static boolean handleEnergyDamage(boolean force, IBlockState state, ItemStack stack, int damage, EntityLivingBase entityLiving)
	{
		if(HammerInfoHandler.isStackDarkSteelHammer(stack))
		{
			return handleDarkHammerDamage(force, state, stack, damage, entityLiving);
		}
		if (EnergyHandler.extractEnergy(stack, ConfigOptions.HammerEnergyUse * damage, true) >= ConfigOptions.HammerEnergyUse * damage)
		{
			EnergyHandler.extractEnergy(stack, ConfigOptions.HammerEnergyUse * damage, false);
		}
		else
		{
			return handleRegularDamage(force, stack, damage, entityLiving);
		}
		return true;
	}

	private static boolean handleDarkHammerDamage(boolean force, IBlockState state, ItemStack stack, int damage, EntityLivingBase entityLiving)
	{
		float chance = UpgradeManager.getChance(getEmpoweredment(stack));

		Random rand = new Random();
		if (rand.nextFloat() < chance)
		{
			if (state == Blocks.OBSIDIAN)
			{
				if (EnergyHandler.extractEnergy(stack, ConfigOptions.EIOToolObsidianEnergyUse, true) >= ConfigOptions.EIOToolObsidianEnergyUse)
				{
					EnergyHandler.extractEnergy(stack, ConfigOptions.EIOToolObsidianEnergyUse, false);
					return true;
				}
			}
			else
			{
				if (EnergyHandler.extractEnergy(stack, ConfigOptions.HammerEnergyUse, true) >= ConfigOptions.HammerEnergyUse)
				{
					EnergyHandler.extractEnergy(stack, ConfigOptions.HammerEnergyUse, false);
					return true;
				}
			}
		}
		return handleRegularDamage(force, stack, damage, entityLiving);
	}

	private static boolean handleManaDamage(boolean force, ItemStack stack, int damage, EntityLivingBase entityLiving)
	{
		if (!ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) entityLiving, MANA_PER_DAMAGE, true))
		{
			return handleRegularDamage(force, stack, damage, entityLiving);
		}
		return true;
	}

	private static boolean handleEmberDamage(boolean force, ItemStack stack, int damage, EntityLivingBase entityLiving)
	{
		if(!(EmberInventoryUtil.getEmberTotal((EntityPlayer) entityLiving) < 0))
		{
			return handleRegularDamage(force, stack, damage, entityLiving);
		}
		return true;
	}

	private static boolean handleLPDamage(boolean force, ItemStack stack, int damage, EntityLivingBase entityLiving){
		if(!((NetworkHelper.getSoulNetwork((EntityPlayer) entityLiving).getCurrentEssence()) < 0)){
			return handleRegularDamage(force, stack, damage, entityLiving);
		}
		return true;
	}

	public static boolean handleDWDamage(boolean force, ItemStack stack, int damage, EntityLivingBase entityLiving){
		if(!(PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.DEFAULT,(EntityPlayer)entityLiving) < 0)){
			return handleRegularDamage(force, stack, damage, entityLiving);
		}
		return true;
	}


	public static boolean handleDamage(boolean force, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int amount)
	{
		return requestDamage(force, null, stack, (EntityPlayer)attacker, amount);
	}

}
