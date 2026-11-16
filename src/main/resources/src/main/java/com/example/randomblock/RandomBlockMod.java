package com.example.randomblock;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class RandomBlockMod implements ModInitializer {

    @Override
    public void onInitialize() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            // Chỉ chạy ở phía Server và không kích hoạt khi đang ở chế độ Sáng tạo (Creative)
            if (world.isClient() || player.isCreative()) {
                return;
            }

            // Lọc danh sách tất cả các khối có item hợp lệ trong game
            List<Block> validBlocks = Registries.BLOCK.stream()
                    .filter(block -> block != Blocks.AIR
                            && block != Blocks.CAVE_AIR
                            && block != Blocks.VOID_AIR
                            && block != Blocks.BEDROCK
                            && block != Blocks.BARRIER
                            && block != Blocks.COMMAND_BLOCK
                            && block != Blocks.CHAIN_COMMAND_BLOCK
                            && block != Blocks.REPEATING_COMMAND_BLOCK
                            && block != Blocks.STRUCTURE_BLOCK
                            && block != Blocks.JIGSAW
                            && block.asItem() != Items.AIR)
                    .toList();

            if (!validBlocks.isEmpty()) {
                Random random = world.getRandom();
                Block randomBlock = validBlocks.get(random.nextInt(validBlocks.size()));
                Item dropItem = randomBlock.asItem();

                // Rớt 1 khối ngẫu nhiên tại vị trí vừa đào
                ItemScatterer.spawn(
                        world,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        new ItemStack(dropItem, 1)
                );
            }
        });
    }
}
