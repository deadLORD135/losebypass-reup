/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.creativetab;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
    public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks"){

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.brick_block);
        }
    };
    public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations"){

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.double_plant);
        }

        @Override
        public int getIconItemDamage() {
            return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
        }
    };
    public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone"){

        @Override
        public Item getTabIconItem() {
            return Items.redstone;
        }
    };
    public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation"){

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.golden_rail);
        }
    };
    public static final CreativeTabs tabMisc = new CreativeTabs(4, "misc"){

        @Override
        public Item getTabIconItem() {
            return Items.lava_bucket;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL);
    public static final CreativeTabs tabAllSearch = new CreativeTabs(5, "search"){

        @Override
        public Item getTabIconItem() {
            return Items.compass;
        }
    }.setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabs(6, "food"){

        @Override
        public Item getTabIconItem() {
            return Items.apple;
        }
    };
    public static final CreativeTabs tabTools = new CreativeTabs(7, "tools"){

        @Override
        public Item getTabIconItem() {
            return Items.iron_axe;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
    public static final CreativeTabs tabCombat = new CreativeTabs(8, "combat"){

        @Override
        public Item getTabIconItem() {
            return Items.golden_sword;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON);
    public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing"){

        @Override
        public Item getTabIconItem() {
            return Items.potionitem;
        }
    };
    public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials"){

        @Override
        public Item getTabIconItem() {
            return Items.stick;
        }
    };
    public static final CreativeTabs tabInventory = new CreativeTabs(11, "inventory"){

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.chest);
        }
    }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;

    public CreativeTabs(int index, String label) {
        this.tabIndex = index;
        this.tabLabel = label;
        CreativeTabs.creativeTabArray[index] = this;
    }

    public int getTabIndex() {
        return this.tabIndex;
    }

    public String getTabLabel() {
        return this.tabLabel;
    }

    public String getTranslatedTabLabel() {
        return "itemGroup." + this.getTabLabel();
    }

    public ItemStack getIconItemStack() {
        if (this.iconItemStack != null) return this.iconItemStack;
        this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        return this.iconItemStack;
    }

    public abstract Item getTabIconItem();

    public int getIconItemDamage() {
        return 0;
    }

    public String getBackgroundImageName() {
        return this.theTexture;
    }

    public CreativeTabs setBackgroundImageName(String texture) {
        this.theTexture = texture;
        return this;
    }

    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }

    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }

    public int getTabColumn() {
        return this.tabIndex % 6;
    }

    public boolean isTabInFirstRow() {
        if (this.tabIndex >= 6) return false;
        return true;
    }

    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }

    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType ... types) {
        this.enchantmentTypes = types;
        return this;
    }

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType enchantmentType) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        EnumEnchantmentType[] enumEnchantmentTypeArray = this.enchantmentTypes;
        int n = enumEnchantmentTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumEnchantmentType enumenchantmenttype = enumEnchantmentTypeArray[n2];
            if (enumenchantmenttype == enchantmentType) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public void displayAllReleventItems(List<ItemStack> p_78018_1_) {
        Iterator<Item> iterator = Item.itemRegistry.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (this.getRelevantEnchantmentTypes() == null) return;
                this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
                return;
            }
            Item item = iterator.next();
            if (item == null || item.getCreativeTab() != this) continue;
            item.getSubItems(item, this, p_78018_1_);
        }
    }

    public void addEnchantmentBooksToList(List<ItemStack> itemList, EnumEnchantmentType ... enchantmentType) {
        Enchantment[] enchantmentArray = Enchantment.enchantmentsBookList;
        int n = enchantmentArray.length;
        int n2 = 0;
        while (n2 < n) {
            Enchantment enchantment = enchantmentArray[n2];
            if (enchantment != null && enchantment.type != null) {
                boolean flag = false;
                for (int i = 0; i < enchantmentType.length && !flag; ++i) {
                    if (enchantment.type != enchantmentType[i]) continue;
                    flag = true;
                }
                if (flag) {
                    itemList.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
            ++n2;
        }
    }
}

