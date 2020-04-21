package cy.jdkdigital.productivebees.entity.bee.hive;

import cy.jdkdigital.productivebees.ProductiveBees;
import cy.jdkdigital.productivebees.entity.bee.IBeeEntity;
import cy.jdkdigital.productivebees.entity.bee.ISolitaryBeeEntity;
import cy.jdkdigital.productivebees.entity.bee.ProductiveBeeEntity;
import cy.jdkdigital.productivebees.init.ModPointOfInterestTypes;
import cy.jdkdigital.productivebees.init.ModTags;
import cy.jdkdigital.productivebees.util.BeeAttributes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EnderBeeEntity extends ProductiveBeeEntity implements IBeeEntity, ISolitaryBeeEntity {
	private int teleportTime = 150;

	public EnderBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
		super(entityType, world);
		this.nestBlockTag = ModTags.getTag(ModTags.END_NESTS);
		beeAttributes.put(BeeAttributes.FOOD_SOURCE, ModTags.getTag(ModTags.END_FLOWERS));
	}

	@Override
	public void tick() {
		super.tick();
		this.teleportTime++;
	}

	@Override
	protected void updateAITasks() {
		// Teleport to active path
		if (this.teleportTime > 150 && null != this.navigator.getPath()) {
			this.teleportTime = 0;
			BlockPos pos = this.navigator.getPath().getTarget();
			teleportTo(pos.getX(), pos.getY(), pos.getZ());
		}

		super.updateAITasks();
	}

	private boolean teleportTo(double x, double y, double z) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

		while(blockpos$mutable.getY() > 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
			blockpos$mutable.move(Direction.DOWN);
		}

		BlockState blockstate = this.world.getBlockState(blockpos$mutable);
		if (blockstate.getMaterial().blocksMovement()) {
			EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
			if (!MinecraftForge.EVENT_BUS.post(event)) {
				boolean teleported = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
				if (teleported) {
					this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 0.3F, 1.0F);
					this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.2F, 1.0F);
				}
				return teleported;
			}
		}
		return false;
	}
}
