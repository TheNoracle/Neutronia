package net.hdt.neutronia.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityShadowPhantom extends EntityLiving {

    public EntityShadowPhantom(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        setSize(0.5F, 0.5F);
    }

    protected void entityInit() {
        super.entityInit();
    }

}