package team.abnormal.neutronia.blocks;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import team.abnormal.abnormalib.interf.IModBlock;
import team.abnormal.neutronia.base.Reference;

public interface INeutroniaBlock extends IModBlock {

    @Override
    default String getModNamespace() {
        return Reference.MOD_ID;
    }

    @Override
    default EnumRarity getBlockRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }

}
