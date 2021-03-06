/*
package team.abnormal.neutronia.init;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import team.abnormal.neutronia.base.utils.registry.Registry;
import team.abnormal.neutronia.village.VillagerData;

public class NDataSerializers {

    public static final DataSerializer<VillagerData> VILLAGER_DATA = new DataSerializer<VillagerData>() {
        @Override
        public void write(PacketBuffer buf, VillagerData value) {
            buf.writeVarInt(Registry.VILLAGER_TYPE.getRawId(value.getType()));
            buf.writeVarInt(Registry.VILLAGER_PROFESSION.getRawId(value.getProfession()));
            buf.writeVarInt(value.getLevel());
        }

        @Override
        public VillagerData read(PacketBuffer buf) {
            return this.write(buf);
        }

        @Override
        public DataParameter<VillagerData> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public VillagerData copyValue(VillagerData value) {
            return value;
        }

        public VillagerData write(PacketBuffer packetByteBuf_1) {
            return new VillagerData(Registry.VILLAGER_TYPE.getInt(packetByteBuf_1.readVarInt()), Registry.VILLAGER_PROFESSION.getInt(packetByteBuf_1.readVarInt()), packetByteBuf_1.readVarInt());
        }

    };

}
*/
