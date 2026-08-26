package net.paladins.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.NbtPredicate;
import net.minecraft.advancements.criterion.TradeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.spell_engine.misc.criteria.SpellCastCriteria;
import net.spell_engine.spellbinding.SpellBindingCriteria;
import net.spell_engine.spellbinding.SpellBookCreationCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Generates the Paladin &amp; Priest advancement tree using the standard {@link Advancement.Builder} API.
 * <p>
 * The advancements live in the shared {@code rpg_series} namespace (they are part of the RPG Series
 * progression UI) but their content is provided by the Paladins mod. {@link #entries()} is the single
 * source of truth: this provider exports each built {@link AdvancementHolder}, and
 * {@code PaladinsDataGenerator.LangGen} reads the same list for the
 * {@code advancements.rpg_series.<path>.title/description} translation keys, which are derived from the
 * advancement id (see {@link #translationKey}). Every advancement id therefore matches its translation
 * suffix — e.g. {@code trade_with_monk} rather than a separate file name and key.
 */
public class PaladinsAdvancements extends FabricAdvancementProvider {
    public static final String NAMESPACE = "rpg_series";

    public PaladinsAdvancements(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        for (var entry : entries()) {
            consumer.accept(entry.entry());
        }
    }

    /** A generated advancement paired with the plain-text strings behind its (id-derived) translation keys. */
    public record Entry(AdvancementHolder entry, String title, String description) {
        public String titleKey() { return translationKey(entry.id(), "title"); }
        public String descriptionKey() { return translationKey(entry.id(), "description"); }
    }

    static String translationKey(Identifier id, String suffix) {
        return "advancements." + id.getNamespace() + "." + id.getPath() + "." + suffix;
    }

    // MARK: Definitions

    private static List<Entry> entries;

    /** Lazily builds the advancement tree on first use (during data generation, when registries are ready). */
    public static List<Entry> entries() {
        if (entries != null) {
            return entries;
        }
        var list = new ArrayList<Entry>();

        // Class choice — create a spell book
        list.add(task("path_choose_paladin", "rpg_series:classes", spellBookIcon("paladin"),
                "book", spellBookCreation("paladins:spell_book/paladin"),
                "Path of Justice", "Create a Paladin Libram"));
        list.add(task("path_choose_priest", "rpg_series:classes", spellBookIcon("priest"),
                "book", spellBookCreation("paladins:spell_book/priest"),
                "Path of Light", "Create a Holy Book"));

        // Obtain the first spell of a class — bind a single spell
        list.add(task("spell_novice_paladin", "rpg_series:path_choose_paladin", item("paladins:iron_mace"),
                "bind", spellBinding("paladins:spell_book/paladin", false),
                "Ardent Defender", "Obtain your first Paladin Spell"));
        list.add(task("spell_novice_priest", "rpg_series:path_choose_priest", item("paladins:holy_wand"),
                "bind", spellBinding("paladins:spell_book/priest", false),
                "Promise of Ascension", "Obtain your first Priest Spell"));

        // Obtain all spells of a class — complete binding
        list.add(challenge("spell_master_paladin", "rpg_series:spell_novice_paladin", item("paladins:golden_great_hammer"),
                "cast", spellBinding("paladins:spell_book/paladin", true),
                "Judgement Time", "Obtain all Paladin Spells"));
        list.add(challenge("spell_master_priest", "rpg_series:spell_novice_priest", item("paladins:holy_staff"),
                "cast", spellBinding("paladins:spell_book/priest", true),
                "Servant of the Ori", "Obtain all Priest Spells"));

        // Cast a spell from a spell book (silent — no toast or chat announcement)
        list.add(silent("spell_cast_paladin_book", "rpg_series:spell_novice_paladin", spellBookIcon("paladin"),
                "cast", spellCast("#paladins:spell_book/paladin"),
                "Paladin Training", "Cast a spell from the Paladin Libram"));
        list.add(silent("spell_cast_priest_book", "rpg_series:spell_novice_priest", spellBookIcon("priest"),
                "cast", spellCast("#paladins:spell_book/priest"),
                "Holy Empowerment", "Cast a spell from the Holy Book"));
        // Cast the wand's primary healing spell
        list.add(silent("spell_cast_priest_wand", "rpg_series:path_choose_priest", item("paladins:acolyte_wand"),
                "cast", spellCast("paladins:heal"),
                "Heals for Feels", "Cast a spell with a healing wand"));

        // Trade with the Monk villager
        list.add(secret("trade_with_monk", "rpg_series:misc_items", item("runes:healing_stone"),
                "trade_with_monk", villagerTrade("paladins:monk"),
                "Healers Exchange", "Purchase an item from a Monk Villager"));

        entries = list;
        return entries;
    }

    // MARK: Builder shorthands

    /** Visible task: toast + chat announcement on. */
    private static Entry task(String idPath, String parent, ItemStack icon,
                              String criterionName, Criterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementType.TASK, true, true, false, criterionName, criterion, title, description);
    }

    /** Challenge-framed task. */
    private static Entry challenge(String idPath, String parent, ItemStack icon,
                                   String criterionName, Criterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementType.CHALLENGE, true, true, false, criterionName, criterion, title, description);
    }

    /** Task without toast or chat announcement. */
    private static Entry silent(String idPath, String parent, ItemStack icon,
                                String criterionName, Criterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementType.TASK, false, false, false, criterionName, criterion, title, description);
    }

    /** Hidden task: not shown in the tree until earned. */
    private static Entry secret(String idPath, String parent, ItemStack icon,
                                String criterionName, Criterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementType.TASK, true, true, true, criterionName, criterion, title, description);
    }

    @SuppressWarnings("deprecation") // Advancement.Builder.parent(Identifier) is the only way to reference parents built outside this provider.
    private static Entry advancement(String idPath, String parent, ItemStack icon, AdvancementType frame,
                                     boolean showToast, boolean announceToChat, boolean hidden,
                                     String criterionName, Criterion<?> criterion, String title, String description) {
        var id = Identifier.fromNamespaceAndPath(NAMESPACE, idPath);
        // The original data-pack advancements did not send telemetry events (vanilla default is off),
        // so keep them untelemetered rather than using the telemetered Advancement.Builder.create().
        var entry = Advancement.Builder.recipeAdvancement()
                .parent(Identifier.parse(parent))
                .display(
                        icon,
                        Component.translatable(translationKey(id, "title")),
                        Component.translatable(translationKey(id, "description")),
                        null,
                        frame,
                        showToast,
                        announceToChat,
                        hidden)
                .addCriterion(criterionName, criterion)
                .build(id);
        return new Entry(entry, title, description);
    }

    // MARK: Icon helpers

    private static ItemStack item(String itemId) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(itemId)));
    }

    private static ItemStack spellBookIcon(String book) {
        var stack = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("spell_engine", "spell_book")));
        stack.set(net.minecraft.core.component.DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath("paladins", "spell_book/" + book)); // vanilla item-model definition = pool id (assets/paladins/items/spell_book/<pool>.json)
        return stack;
    }

    // MARK: Criterion helpers

    private static Criterion<?> spellBookCreation(String spellPool) {
        return SpellBookCreationCriteria.INSTANCE.createCriterion(
                new SpellBookCreationCriteria.Condition(Optional.empty(), Optional.of(spellPool)));
    }

    private static Criterion<?> spellBinding(String spellPool, boolean complete) {
        return SpellBindingCriteria.INSTANCE.createCriterion(
                new SpellBindingCriteria.Condition(Optional.empty(), Optional.of(spellPool), Optional.of(complete)));
    }

    private static Criterion<?> spellCast(String spell) {
        return SpellCastCriteria.INSTANCE.createCriterion(
                new SpellCastCriteria.Condition(Optional.empty(), Optional.of(spell), Optional.empty()));
    }

    private static Criterion<?> villagerTrade(String profession) {
        var villagerData = new CompoundTag();
        villagerData.putString("profession", profession);
        var nbt = new CompoundTag();
        nbt.put("VillagerData", villagerData);
        var villager = EntityPredicate.wrap(
                EntityPredicate.Builder.entity().nbt(new NbtPredicate(nbt)));
        return CriteriaTriggers.TRADE.createCriterion(
                new TradeTrigger.TriggerInstance(Optional.empty(), Optional.of(villager), Optional.empty()));
    }
}
