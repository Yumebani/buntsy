package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.sophiebun.buntsy.BuntsyMod;
import net.sophiebun.buntsy.blocks.inventory.FilterSlot;
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenInteractionConfig;
import net.sophiebun.buntsy.server.CMTParticipantPacket;
import net.sophiebun.buntsy.server.ModPacketHandler;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CMTParticipantScreen extends AbstractContainerScreen<CMTParticipantMenu> {

    public static final int MAX_CHANNELS = 16;
    private int channelEdit = 0;
    private Button channelPanel;

    private boolean editingInsert = true;
    private Button insertExtractPanel;

    private final List<AbstractWidget> sideConfigButtons = new ArrayList<>();

    private Button enableDisableButton;

    private Button priorityPanel;
    private final List<AbstractWidget> priorityConfigButtons = new ArrayList<>();
    private Button fillRegimeButton;

    private Button extractCountPanel;
    private final List<AbstractWidget> extractCountConfigButtons = new ArrayList<>();
    private AbstractWidget selectionRegimeButton;

    private Button whiteListButton;
    private Button setExactButton;

    private int modifier = 1;

    private  List<Direction> availableSides;

    private  BlockPos pos;
    private  BlockPos terminal;

    private  CMTParticipantData data;

    private int xGlobal;
    private int yGlobal;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/clockwork_maiden_terminal_gui.png");

    public CMTParticipantScreen(CMTParticipantMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 207;
        this.imageWidth = 192;

        this.pos = null;
        this.terminal = null;
        this.data = null;
        this.availableSides = null;
    }

    public CMTParticipantScreen loadData(BlockPos pos, BlockPos terminal, CMTParticipantData data, List<Direction> availableSides){
        this.pos = pos;
        this.terminal = terminal;
        this.data = data;
        this.availableSides = availableSides;
        return this;
    }

    private CMTParticipantScreen(CMTParticipantMenu pMenu, Inventory pPlayerInventory, BlockPos pos, BlockPos terminal, CMTParticipantData data, List<Direction> availableSides) {
        super(pMenu, pPlayerInventory, Component.translatable("screen.cmt_participant"));
        this.imageHeight = 205;
        this.imageWidth = 192;

        this.pos = pos;
        this.terminal = terminal;
        this.data = data;
        this.availableSides = availableSides;
    }

    @Override
    public void onClose() {
        saveCurrentWhiteList();
        ModPacketHandler.INSTANCE.sendToServer(new CMTParticipantPacket(this.data, this.terminal, this.pos));
        super.onClose();
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 37;
        this.titleLabelY = 2;
        this.inventoryLabelY = 117;

        xGlobal = (width - imageWidth) / 2;
        yGlobal = (height - imageHeight) / 2;

        addChannelPanel();
        addInsertExtractEdit();
        addToggleButton();
        addSideConfigButtons();
        addWhiteListButtons();

        addPriorityButtons();
        addFillRegimeButton();

        addStackSizeButtons();
        addSelectionRegimeButton();
    }

    private void changeChannel(int amount){

        saveCurrentWhiteList();
        channelEdit += amount;
        if (channelEdit < 0){
            channelEdit = MAX_CHANNELS - 1;
        } else if (channelEdit >= MAX_CHANNELS){
            channelEdit = 0;
        }
        loadWhiteList();
    }

    private void addChannelPanel(){

        this.addRenderableWidget(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changeChannel(-1);
                        })
                .bounds(xGlobal + 8, yGlobal + 13, 8, 13)
                .build());

        this.channelPanel = Button.builder(
                        Component.literal("Channel 1"), pButton -> {})
                .bounds(xGlobal + 19, yGlobal + 13, 55, 13)
                .build();

        this.channelPanel.active = false;
        this.addRenderableWidget(this.channelPanel);

        this.addRenderableWidget(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changeChannel(1);
                        })
                .bounds(xGlobal + 77, yGlobal + 13, 8, 13)
                .build());
    }

    private void updateChannelPanel(){
        this.channelPanel.setMessage(Component.literal("Channel " + (this.channelEdit + 1)));
    }

    private void changeInsertExtractEdit(){
        saveCurrentWhiteList();
        editingInsert = !editingInsert;
        loadWhiteList();
    }

    private void loadWhiteList(){

        if (this.data.isEnabled(editingInsert, channelEdit)){
            List<ItemStack> filter = data.getConfig(editingInsert, channelEdit).getFilter();
            for (int i = 36; i < 48; i++){
                ItemStack entry = ItemStack.EMPTY;
                entry.deserializeNBT(filter.get(i - 36).serializeNBT());
                this.menu.slots.get(i).set(entry);
            }
        }
    }

    private void saveCurrentWhiteList(){

        if (this.data.isEnabled(editingInsert, channelEdit)){
            List<ItemStack> filter = data.getConfig(editingInsert, channelEdit).getFilter();
            filter.clear();
            for (int i = 36; i < 48; i++){
                if (this.menu.slots.get(i).hasItem()){
                    ItemStack entry = ItemStack.EMPTY;
                    entry.deserializeNBT(this.menu.slots.get(i).getItem().serializeNBT());
                    filter.add(entry);
                } else {
                    filter.add(ItemStack.EMPTY);
                }
            }
            setChanged();
        }
    }

    private void addInsertExtractEdit(){
        this.insertExtractPanel = Button.builder(
                        Component.literal("Insert"), pButton -> {
                            changeInsertExtractEdit();
                        })
                .bounds(xGlobal + 90, yGlobal + 13, 45, 13)
                .build();

        this.addRenderableWidget(this.insertExtractPanel);
    }

    private void updateInsertExtractPanel(){
        this.insertExtractPanel.setMessage(Component.literal(this.editingInsert ? "Insert" : "Extract"));
    }

    private void setSide(Direction side){
        data.getConfig(editingInsert, channelEdit).setSide(side);
        setChanged();
    }

    private Direction getSide(){
        return data.getConfig(editingInsert, channelEdit).getSide();
    }

    private void setChanged(){
        data.setChanged(editingInsert, channelEdit);
    }

    private void addSideConfigButtons(){

        Button button;

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.DOWN);
                        })
                .bounds(xGlobal + 8, yGlobal + 93, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.DOWN);
        this.sideConfigButtons.add(button);

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.UP);
                        })
                .bounds(xGlobal + 54, yGlobal + 47, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.UP);
        this.sideConfigButtons.add(button);

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.NORTH);
                        })
                .bounds(xGlobal + 31, yGlobal + 47, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.NORTH);
        this.sideConfigButtons.add(button);

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.SOUTH);
                        })
                .bounds(xGlobal + 31, yGlobal + 93, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.SOUTH);
        this.sideConfigButtons.add(button);

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.WEST);
                        })
                .bounds(xGlobal + 8, yGlobal + 70, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.WEST);
        this.sideConfigButtons.add(button);

        button = Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.EAST);
                        })
                .bounds(xGlobal + 54, yGlobal + 70, 20, 20)
                .build();

        button.active = availableSides.contains(Direction.EAST);
        this.sideConfigButtons.add(button);

        for (AbstractWidget widget : this.sideConfigButtons){
            this.addRenderableWidget(widget);
        }
    }

    private void updateSideConfigButtons(){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            for (int i = 0; i < 6 ; i++){
                this.sideConfigButtons.get(i).visible = true;
                if (i == getSide().ordinal()){
                    this.sideConfigButtons.get(i).setFGColor(0xAAAAFF);
                }
                else {
                    this.sideConfigButtons.get(i).setFGColor(0xFFFFFF);
                }
            }
        } else {
            for (AbstractWidget widget : this.sideConfigButtons){
                widget.visible = false;
            }
        }
    }

    private void renderSideConfigButtonsIcons(GuiGraphics guiGraphics){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            MaidenInteractionConfig config = this.data.getConfig(editingInsert, channelEdit);

            if (availableSides.contains(Direction.DOWN)){
                if (config.getSide() == Direction.DOWN){
                    guiGraphics.blit(TEXTURE, xGlobal + 8, yGlobal + 93, 213, 0, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 8, yGlobal + 93, 193, 0, 20, 20);
                }
            }

            if (availableSides.contains(Direction.UP)){
                if (config.getSide() == Direction.UP){
                    guiGraphics.blit(TEXTURE, xGlobal + 54, yGlobal + 47, 213, 20, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 54, yGlobal + 47, 193, 20, 20, 20);
                }
            }

            if (availableSides.contains(Direction.NORTH)){
                if (config.getSide() == Direction.NORTH){
                    guiGraphics.blit(TEXTURE, xGlobal + 31, yGlobal + 47, 213, 40, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 31, yGlobal + 47, 193, 40, 20, 20);
                }
            }

            if (availableSides.contains(Direction.SOUTH)){
                if (config.getSide() == Direction.SOUTH){
                    guiGraphics.blit(TEXTURE, xGlobal + 31, yGlobal + 93, 213, 60, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 31, yGlobal + 93, 193, 60, 20, 20);
                }
            }

            if (availableSides.contains(Direction.WEST)){
                if (config.getSide() == Direction.WEST){
                    guiGraphics.blit(TEXTURE, xGlobal + 8, yGlobal + 70, 213, 80, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 8, yGlobal + 70, 193, 80, 20, 20);
                }
            }

            if (availableSides.contains(Direction.EAST)){
                if (config.getSide() == Direction.EAST){
                    guiGraphics.blit(TEXTURE, xGlobal + 54, yGlobal + 70, 213, 100, 20, 20);
                } else {
                    guiGraphics.blit(TEXTURE, xGlobal + 54, yGlobal + 70, 193, 100, 20, 20);
                }
            }}
    }

    private void toggle(){
        this.data.setEnabled(editingInsert, channelEdit, !this.data.isEnabled(editingInsert, channelEdit));
        if (this.data.isEnabled(editingInsert, channelEdit)){
            data.checkOrMakeChannel(editingInsert, channelEdit, pos);
            loadWhiteList();
        } else {
            saveCurrentWhiteList();
        }
        setChanged();
    }

    private void changePriority(int amount){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.setPriority(Math.max(Math.min(config.getPriority() + amount, 999), 0));
        setChanged();
    }

    private void cycleFillRegime(){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.cycleFillRegime();
        setChanged();
    }

    private void addFillRegimeButton(){

        this.fillRegimeButton = Button.builder(
                        Component.literal("One stack"), pButton -> {
                            cycleFillRegime();
                        })
                .bounds(xGlobal + 111, yGlobal + 30, 60, 13)
                .tooltip(Tooltip.create(Component.literal("Change fill regime")))
                .build();

        this.addRenderableWidget(fillRegimeButton);
    }

    private void updateFillRegimeButton(){
        if (editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            this.fillRegimeButton.visible = true;
            this.fillRegimeButton.setMessage(Component.literal(
                    switch (this.data.getConfig(editingInsert, channelEdit).getFillRegime()){
                        case ONE -> "One item";
                        case STACK -> "One stack";
                        case FILL -> "Fill";
                    }
            ));
        } else {
            this.fillRegimeButton.visible = false;
        }
    }

    private void addPriorityButtons(){

        this.priorityConfigButtons.add(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changePriority(-modifier);
                        })
                .bounds(xGlobal + 61, yGlobal + 30, 8, 13)
                .build());

        this.priorityPanel = Button.builder(
                        Component.literal("0"), pButton -> {})
                .bounds(xGlobal + 72, yGlobal + 30, 23, 13)
                .build();

        this.priorityPanel.active = false;
        this.priorityConfigButtons.add(this.priorityPanel);

        this.priorityConfigButtons.add(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changePriority(modifier);
                        })
                .bounds(xGlobal + 98, yGlobal + 30, 8, 13)
                .build());

        for (AbstractWidget widget : this.priorityConfigButtons){
            this.addRenderableWidget(widget);
        }
    }

    private void updatePriorityButtons(){
        if (editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            for (int i = 0; i < 3 ; i++){
                this.priorityConfigButtons.get(i).visible = true;
            }
            this.priorityPanel.setMessage(Component.literal( "" + data.getConfig(editingInsert, channelEdit).getPriority()));
        } else {
            for (AbstractWidget widget : this.priorityConfigButtons){
                widget.visible = false;
            }
        }
    }

    private void changeStackSize(int amount){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.setExtractCount(Math.max(Math.min(config.getExtractCount() + amount, 64), 1 ));
        setChanged();
    }

    private void cycleSelectionRegime(){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.cycleSelectionRegime();
        setChanged();
    }

    private void addSelectionRegimeButton(){

        this.selectionRegimeButton = Button.builder(
                        Component.literal("Nearest"), pButton -> {
                            cycleSelectionRegime();
                        })
                .bounds(xGlobal + 111, yGlobal + 30, 60, 13)
                .tooltip(Tooltip.create(Component.literal("Change distribution regime")))
                .build();

        this.addRenderableWidget(selectionRegimeButton);
    }

    private void updateSelectionRegimeButton(){
        if (!editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            this.selectionRegimeButton.visible = true;
            this.selectionRegimeButton.setMessage(Component.literal(
                    switch (this.data.getConfig(editingInsert, channelEdit).getSelectionRegime()){
                        case NEAREST -> "Nearest";
                        case ROUND_ROBIN -> "Round robin";
                        case PRIORITY -> "Priority";
                    }
            ));
        } else {
            this.selectionRegimeButton.visible = false;
        }
    }

    private void addStackSizeButtons(){

        this.extractCountConfigButtons.add(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changeStackSize(modifier < 10 ? 1 : modifier < 100 ? 4 : 16);
                        })
                .bounds(xGlobal + 61, yGlobal + 30, 8, 13)
                .build());

        this.extractCountPanel = Button.builder(
                        Component.literal("0"), pButton -> {})
                .bounds(xGlobal + 72, yGlobal + 30, 23, 13)
                .build();

        this.extractCountPanel.active = false;
        this.extractCountConfigButtons.add(this.extractCountPanel);

        this.extractCountConfigButtons.add(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changeStackSize(modifier < 10 ? -1 : modifier < 100 ? -4 : -16);
                        })
                .bounds(xGlobal + 98, yGlobal + 30, 8, 13)
                .build());

        for (AbstractWidget widget : this.extractCountConfigButtons){
            this.addRenderableWidget(widget);
        }
    }

    private void updateStackSizeButtons(){
        if (!editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            for (int i = 0; i < 3 ; i++){
                this.extractCountConfigButtons.get(i).visible = true;
            }
            this.extractCountPanel.setMessage(Component.literal("" + data.getConfig(editingInsert, channelEdit).getExtractCount()));
        } else {
            for (AbstractWidget widget : this.extractCountConfigButtons){
                widget.visible = false;
            }
        }
    }

    private void toggleWhiteList(){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.setWhiteList(!config.getWhiteList());
        setChanged();
    }

    private void toggleExact(){
        MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
        config.setExact(!config.getExact());
        setChanged();
    }

    private void addWhiteListButtons(){

        this.whiteListButton = Button.builder(
                        Component.literal(""), pButton -> {
                            toggleWhiteList();
                        })
                .bounds(xGlobal + 167, yGlobal + 59, 18, 18)
                .tooltip(Tooltip.create(Component.literal("Set whitelist")))
                .build();

        this.addRenderableWidget(this.whiteListButton);

        this.setExactButton = Button.builder(
                        Component.literal(""), pButton -> {
                            toggleExact();
                        })
                .bounds(xGlobal + 167, yGlobal + 85, 18, 18)
                .tooltip(Tooltip.create(Component.literal("Set exact")))
                .build();

        this.addRenderableWidget(this.setExactButton);
    }

    private void updateWhiteListButtons(){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            MaidenInteractionConfig config = data.getConfig(editingInsert, channelEdit);
            this.whiteListButton.visible = true;
            this.whiteListButton.setTooltip(Tooltip.create(Component.literal(config.getWhiteList() ? "Set blacklist" : "Set whitelist")));
            this.setExactButton.visible = true;
            this.setExactButton.setTooltip(Tooltip.create(Component.literal(config.getExact() ? "Set fuzzy" : "Set exact")));
        } else {
            this.whiteListButton.visible = false;
            this.setExactButton.visible = false;
        }
    }

    private void updateToggleButton(){
        this.enableDisableButton.setMessage(Component.literal(this.data.isEnabled(editingInsert, channelEdit) ? "Disable" : "Enable"));
    }

    private void addToggleButton(){

        this.enableDisableButton = Button.builder(
                        Component.literal("Enable"), pButton -> {
                            toggle();
                        })
                .bounds(xGlobal + 140, yGlobal + 13, 45, 13)
                .build();

        this.addRenderableWidget(this.enableDisableButton);
    }

    private void renderPriorityText(GuiGraphics guiGraphics){
        if (editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            guiGraphics.drawString(this.font, Component.literal("Priority"), xGlobal + 16, yGlobal + 31, 0xFFFFFFFF, true);
        }
    }

    private void renderStacksizeText(GuiGraphics guiGraphics){
        if (!editingInsert && this.data.isEnabled(editingInsert, channelEdit)){
            guiGraphics.drawString(this.font, Component.literal("Extract size"), xGlobal + 6, yGlobal + 31, 0xFFFFFFFF, true);
        }
    }

    private void renderFilterIcons(GuiGraphics guiGraphics){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            if (this.data.getConfig(editingInsert, channelEdit).getWhiteList()){
                guiGraphics.blit(TEXTURE, xGlobal + 167, yGlobal + 59, 193, 138, 18, 18);
            } else {
                guiGraphics.blit(TEXTURE, xGlobal + 167, yGlobal + 59, 193, 120, 18, 18);
            }

            if (this.data.getConfig(editingInsert, channelEdit).getExact()){
                guiGraphics.blit(TEXTURE, xGlobal + 167, yGlobal + 85, 193, 174, 18, 18);
            } else {
                guiGraphics.blit(TEXTURE, xGlobal + 167, yGlobal + 85, 193, 156, 18, 18);
            }
        }
    }

    private void renderFilterGrid(GuiGraphics guiGraphics){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            guiGraphics.blit(TEXTURE, xGlobal + 85, yGlobal + 54, 15, 125, 18 * 4, 18 * 3);
        }
    }

    private void updateSlots(){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            for (int i = 36; i < 36 + 12; i++){
                ((FilterSlot) this.menu.getSlot(i)).setActive(true);
            }
        } else {
            for (int i = 36; i < 36 + 12; i++){
                ((FilterSlot) this.menu.getSlot(i)).setActive(false);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderFilterGrid(guiGraphics);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        updateChannelPanel();
        updateInsertExtractPanel();
        updateSideConfigButtons();
        updateToggleButton();
        updateWhiteListButtons();

        updateFillRegimeButton();
        updatePriorityButtons();

        updateSelectionRegimeButton();
        updateStackSizeButtons();

        updateSlots();

        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);

        renderSideConfigButtonsIcons(guiGraphics);
        renderPriorityText(guiGraphics);
        renderStacksizeText(guiGraphics);
        renderFilterIcons(guiGraphics);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {

        if (pKeyCode == GLFW.GLFW_KEY_LEFT_SHIFT || pKeyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.modifier *= 10;
            return true;
        }

        if (pKeyCode == GLFW.GLFW_KEY_LEFT_CONTROL || pKeyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.modifier *= 10;
            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {

        if (pKeyCode == GLFW.GLFW_KEY_LEFT_SHIFT || pKeyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.modifier /= 10;
            return true;
        }

        if (pKeyCode == GLFW.GLFW_KEY_LEFT_CONTROL || pKeyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.modifier /= 10;
            return true;
        }

        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }
}
