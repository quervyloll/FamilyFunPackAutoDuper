package family_fun_pack.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BlockUtil implements Wrapper {

    /**
     * @author linustouchtips
     * @since 05/06/2021
     */

    static Minecraft mc = Minecraft.getMinecraft();

    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    public static final List<Block> unSafeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
    public static List<Block> unSolidBlocks = Arrays.asList(Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.AIR, Blocks.PORTAL, Blocks.END_PORTAL, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.SAPLING, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.VINE, Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH);


    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        return sneaking || isSneaking;
    }

    public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.lookAtVec3d(hitVec);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        return sneaking || isSneaking;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());
            BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!BlockUtil.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(neighbour), false) || (blockState = BlockUtil.mc.world.getBlockState(neighbour)).getMaterial().isReplaceable())
                continue;
            facings.add(side);
        }
        return facings;
    }

    public static boolean isAir(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);

        return state.getBlock() == Blocks.AIR || state.getMaterial().isReplaceable();
    }

    public static float getHardness(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);

        return blockState.getBlockHardness(mc.world, pos);
    }

    public static List<Block> emptyBlocks;
    public static List<Block> rightclickableBlocks;

    static {
        emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
        rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
    }

        // All blocks that are resistant to explosions
        public static final List<Block> resistantBlocks = Arrays.asList(
                Blocks.OBSIDIAN,
                Blocks.ANVIL,
                Blocks.ENCHANTING_TABLE,
                Blocks.ENDER_CHEST,
                Blocks.BEACON
        );

        // All blocks that are unbreakable with tools in survival mode
        public static final List<Block> unbreakableBlocks = Arrays.asList(
                Blocks.BEDROCK,
                Blocks.COMMAND_BLOCK,
                Blocks.CHAIN_COMMAND_BLOCK,
                Blocks.END_PORTAL_FRAME,
                Blocks.BARRIER,
                Blocks.PORTAL
        );

        /**
         * Finds the if a given position is breakable
         *
         * @param position The position to check
         * @return Whether or not the given position is breakable
         */
        public static boolean isBreakable(BlockPos position) {
            return !getResistance(position).equals(Resistance.UNBREAKABLE);
        }

        /**
         * Checks if a block is replaceable
         *
         * @param pos the position to check
         * @return if this block pos can be placed at
         */
        public static boolean isReplaceable(BlockPos pos) {
            return mc.world.getBlockState(pos).getMaterial().isReplaceable();
        }

        /**
         * Finds the resistance of a given position
         *
         * @param position The position to find the resistance for
         * @return The {@link Resistance} resistance of the given position
         */
        public static Resistance getResistance(BlockPos position) {

            // the block at the given position
            Block block = mc.world.getBlockState(position).getBlock();

            // idk why this would be null but it throws errors
            if (block != null) {

                // find resistance
                if (resistantBlocks.contains(block)) {
                    return Resistance.RESISTANT;
                } else if (unbreakableBlocks.contains(block)) {
                    return Resistance.UNBREAKABLE;
                } else if (block.getDefaultState().getMaterial().isReplaceable()) {
                    return Resistance.REPLACEABLE;
                } else {
                    return Resistance.BREAKABLE;
                }
            }

            return Resistance.NONE;
        }

        /**
         * Gets the distance to the center of the block
         *
         * @param in The block to get the distance to
         * @return The distance to the center of the block
         */
        public static double getDistanceToCenter(EntityPlayer player, BlockPos in) {

            // distances
            double dX = in.getX() + 0.5 - player.posX;
            double dY = in.getY() + 0.5 - player.posY;
            double dZ = in.getZ() + 0.5 - player.posZ;

            // distance to center
            return StrictMath.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));
        }

        /**
         * Gets all blocks in range from a specified player
         *
         * @param player The player to find the surrounding blocks (anchor)
         * @param area   The area range to consider blocks
         * @return A list of the surrounding blocks
         */
        public static List<BlockPos> getBlocksInArea(EntityPlayer player, AxisAlignedBB area) {
            if (player != null) {

                // list of nearby blocks
                List<BlockPos> blocks = new ArrayList<>();

                // iterate through all surrounding blocks
                for (double x = StrictMath.floor(area.minX); x <= StrictMath.ceil(area.maxX); x++) {
                    for (double y = StrictMath.floor(area.minY); y <= StrictMath.ceil(area.maxY); y++) {
                        for (double z = StrictMath.floor(area.minZ); z <= StrictMath.ceil(area.maxZ); z++) {

                            // the current position
                            BlockPos position = player.getPosition().add(x, y, z);

                            // check distance to block
                            if (getDistanceToCenter(player, position) >= area.maxX) {
                                continue;
                            }

                            // add the block to our list
                            blocks.add(position);
                        }
                    }
                }

                return blocks;
            }

            // rofl, threading is so funny
            return new ArrayList<>();
        }

        // the resistance level of the block
        public enum Resistance {

            /**
             * Blocks that are able to be replaced by other blocks
             */
            REPLACEABLE,

            /**
             * Blocks that are able to be broken with tools in survival mode
             */
            BREAKABLE,

            /**
             * Blocks that are resistant to explosions
             */
            RESISTANT,

            /**
             * Blocks that are unbreakable with tools in survival mode
             */
            UNBREAKABLE,

            /**
             * Null equivalent
             */
            NONE
        }
}

