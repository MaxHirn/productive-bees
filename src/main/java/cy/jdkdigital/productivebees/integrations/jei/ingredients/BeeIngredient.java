package cy.jdkdigital.productivebees.integrations.jei.ingredients;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BeeIngredient
{
    private EntityType<? extends BeeEntity> bee;
    private ResourceLocation beeType;
    private int renderType = 0;
    private boolean configurable = false;

    public BeeIngredient(EntityType<? extends BeeEntity> bee, int renderType) {
        this.bee = bee;
        this.renderType = renderType;
    }

    public BeeIngredient(EntityType<? extends BeeEntity> bee, ResourceLocation beeType, int renderType) {
        this(bee, renderType);
        this.beeType = beeType;
    }

    public BeeIngredient(EntityType<? extends BeeEntity> bee, ResourceLocation beeType, int renderType, boolean isConfigurable) {
        this(bee, renderType);
        this.beeType = beeType;
        this.configurable = isConfigurable;
    }

    public EntityType<? extends BeeEntity> getBeeEntity() {
        return bee;
    }

    public ResourceLocation getBeeType() {
        return beeType != null ? beeType : bee.getRegistryName();
    }

    public int getRenderType() {
        return renderType;
    }

    public static BeeIngredient read(PacketBuffer buffer) {
        String beeName = buffer.readString();

        return new BeeIngredient((EntityType<? extends BeeEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(beeName)), buffer.readInt());
    }

    public final void write(PacketBuffer buffer) {
        buffer.writeString("" + this.bee.getRegistryName());
        buffer.writeInt(this.renderType);
    }

    @Override
    public String toString() {
        return "BeeIngredient{" +
                "bee=" + bee +
                ", beeType=" + beeType +
                ", renderType=" + renderType +
                '}';
    }

    public boolean isConfigurable() {
        return configurable;
    }
}
