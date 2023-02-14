/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");

    public BlockTripWireHook() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(ATTACHED, false).withProperty(SUSPENDED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean bl;
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            bl = true;
            return state.withProperty(SUSPENDED, bl);
        }
        bl = false;
        return state.withProperty(SUSPENDED, bl);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        if (!side.getAxis().isHorizontal()) return false;
        if (!worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube()) return false;
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        EnumFacing enumfacing;
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while (!worldIn.getBlockState(pos.offset(enumfacing = iterator.next())).getBlock().isNormalCube());
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, false).withProperty(ATTACHED, false).withProperty(SUSPENDED, false);
        if (!facing.getAxis().isHorizontal()) return iblockstate;
        return iblockstate.withProperty(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.func_176260_a(worldIn, pos, state, false, false, -1, null);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (neighborBlock == this) return;
        if (!this.checkForDrop(worldIn, pos, state)) return;
        EnumFacing enumfacing = state.getValue(FACING);
        if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().isNormalCube()) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

    public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
        EnumFacing enumfacing = hookState.getValue(FACING);
        boolean flag = hookState.getValue(ATTACHED);
        boolean flag1 = hookState.getValue(POWERED);
        boolean flag2 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        boolean flag3 = !p_176260_4_;
        boolean flag4 = false;
        int i = 0;
        IBlockState[] aiblockstate = new IBlockState[42];
        for (int j = 1; j < 42; ++j) {
            BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.tripwire_hook) {
                if (iblockstate.getValue(FACING) != enumfacing.getOpposite()) break;
                i = j;
                break;
            }
            if (iblockstate.getBlock() != Blocks.tripwire && j != p_176260_6_) {
                aiblockstate[j] = null;
                flag3 = false;
                continue;
            }
            if (j == p_176260_6_) {
                iblockstate = (IBlockState)Objects.firstNonNull((Object)p_176260_7_, (Object)iblockstate);
            }
            boolean flag5 = iblockstate.getValue(BlockTripWire.DISARMED) == false;
            boolean flag6 = iblockstate.getValue(BlockTripWire.POWERED);
            boolean flag7 = iblockstate.getValue(BlockTripWire.SUSPENDED);
            flag3 &= flag7 == flag2;
            flag4 |= flag5 && flag6;
            aiblockstate[j] = iblockstate;
            if (j != p_176260_6_) continue;
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            flag3 &= flag5;
        }
        IBlockState iblockstate1 = this.getDefaultState().withProperty(ATTACHED, flag3).withProperty(POWERED, flag4 &= (flag3 &= i > 1));
        if (i > 0) {
            BlockPos blockpos1 = pos.offset(enumfacing, i);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos1, iblockstate1.withProperty(FACING, enumfacing1), 3);
            this.func_176262_b(worldIn, blockpos1, enumfacing1);
            this.func_180694_a(worldIn, blockpos1, flag3, flag4, flag, flag1);
        }
        this.func_180694_a(worldIn, pos, flag3, flag4, flag, flag1);
        if (!p_176260_4_) {
            worldIn.setBlockState(pos, iblockstate1.withProperty(FACING, enumfacing), 3);
            if (p_176260_5_) {
                this.func_176262_b(worldIn, pos, enumfacing);
            }
        }
        if (flag == flag3) return;
        int k = 1;
        while (k < i) {
            BlockPos blockpos2 = pos.offset(enumfacing, k);
            IBlockState iblockstate2 = aiblockstate[k];
            if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getBlock() != Blocks.air) {
                worldIn.setBlockState(blockpos2, iblockstate2.withProperty(ATTACHED, flag3), 3);
            }
            ++k;
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.func_176260_a(worldIn, pos, state, false, true, -1, null);
    }

    private void func_180694_a(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.6f);
            return;
        }
        if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.5f);
            return;
        }
        if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.click", 0.4f, 0.7f);
            return;
        }
        if (p_180694_3_) return;
        if (!p_180694_5_) return;
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5, "random.bowhit", 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
    }

    private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (this.canPlaceBlockAt(worldIn, pos)) return true;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        float f = 0.1875f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[worldIn.getBlockState(pos).getValue(FACING).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                return;
            }
            case 2: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                return;
            }
            case 3: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                return;
            }
            case 4: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                return;
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = state.getValue(ATTACHED);
        boolean flag1 = state.getValue(POWERED);
        if (flag || flag1) {
            this.func_176260_a(worldIn, pos, state, true, false, -1, null);
        }
        if (flag1) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (state.getValue(POWERED) == false) return 0;
        return 15;
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        if (state.getValue(FACING) != side) return 0;
        return 15;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(POWERED, (meta & 8) > 0);
        if ((meta & 4) > 0) {
            bl = true;
            return iBlockState.withProperty(ATTACHED, bl);
        }
        bl = false;
        return iBlockState.withProperty(ATTACHED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        if (state.getValue(ATTACHED) == false) return i;
        i |= 4;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, POWERED, ATTACHED, SUSPENDED);
    }
}

