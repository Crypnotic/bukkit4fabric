package org.bukkit.craftbukkit.block;

import com.fungus_soft.bukkitfabric.interfaces.block.IMixinBannerBlockEntity;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public class CraftBanner extends CraftBlockEntityState<BannerBlockEntity> implements Banner {

    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(final Block block) {
        super(block, BannerBlockEntity.class);
    }

    public CraftBanner(final Material material, final BannerBlockEntity te) {
        super(material, te);
    }

    @Override
    public void load(BannerBlockEntity banner) {
        super.load(banner);

        base = DyeColor.getByWoolData((byte) ((AbstractBannerBlock) this.data.getBlock()).getColor().getId());
        patterns = new ArrayList<Pattern>();
        IMixinBannerBlockEntity ibanner = ((IMixinBannerBlockEntity)(Object)banner);

        if (ibanner.patternListTag() != null) {
            for (int i = 0; i < ibanner.patternListTag().size(); i++) {
                CompoundTag p = (CompoundTag) ibanner.patternListTag().get(i);
                patterns.add(new Pattern(DyeColor.getByWoolData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "color");
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return this.patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return this.patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    public void applyTo(BannerBlockEntity banner) {
        super.applyTo(banner);

        ((IMixinBannerBlockEntity)(Object)banner).setBaseColor(net.minecraft.util.DyeColor.byId(base.getWoolData()));

        ListTag newPatterns = new ListTag();

        for (Pattern p : patterns) {
            CompoundTag compound = new CompoundTag();
            compound.putInt("Color", p.getColor().getWoolData());
            compound.putString("Pattern", p.getPattern().getIdentifier());
            newPatterns.add(compound);
        }
        ((IMixinBannerBlockEntity)(Object)banner).setPatternListTag(newPatterns);
    }

}