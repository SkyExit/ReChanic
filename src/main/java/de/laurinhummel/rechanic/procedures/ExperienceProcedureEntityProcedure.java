package de.laurinhummel.rechanic.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;

import java.util.Map;
import java.util.HashMap;

import de.laurinhummel.rechanic.enchantment.ExperienceEnchantment;
import de.laurinhummel.rechanic.RechanicModElements;
import de.laurinhummel.rechanic.RechanicMod;

@RechanicModElements.ModElement.Tag
public class ExperienceProcedureEntityProcedure extends RechanicModElements.ModElement {
	public ExperienceProcedureEntityProcedure(RechanicModElements instance) {
		super(instance, 5);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("sourceentity") == null) {
			if (!dependencies.containsKey("sourceentity"))
				RechanicMod.LOGGER.warn("Failed to load dependency sourceentity for procedure ExperienceProcedureEntity!");
			return;
		}
		Entity sourceentity = (Entity) dependencies.get("sourceentity");
		if (((EnchantmentHelper.getEnchantmentLevel(ExperienceEnchantment.enchantment,
				((sourceentity instanceof LivingEntity) ? ((LivingEntity) sourceentity).getHeldItemMainhand() : ItemStack.EMPTY))) > 0)) {
			if (sourceentity instanceof PlayerEntity)
				((PlayerEntity) sourceentity).giveExperiencePoints((int) ((EnchantmentHelper.getEnchantmentLevel(ExperienceEnchantment.enchantment,
						((sourceentity instanceof LivingEntity) ? ((LivingEntity) sourceentity).getHeldItemMainhand() : ItemStack.EMPTY))) * 1));
		}
	}

	@SubscribeEvent
	public void onLivingDropXp(LivingExperienceDropEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity entity = event.getEntity();
			double i = entity.getPosX();
			double j = entity.getPosY();
			double k = entity.getPosZ();
			PlayerEntity attacked = event.getAttackingPlayer();
			int droppedxp = (int) event.getDroppedExperience();
			int originalxp = (int) event.getOriginalExperience();
			World world = entity.world;
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("droppedexperience", droppedxp);
			dependencies.put("originalexperience", originalxp);
			dependencies.put("sourceentity", attacked);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("event", event);
			this.executeProcedure(dependencies);
		}
	}
}
