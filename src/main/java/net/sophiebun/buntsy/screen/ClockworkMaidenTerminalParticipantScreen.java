package net.sophiebun.buntsy.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
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
import net.sophiebun.buntsy.entity.clockwork_maiden.CMTParticipantData;
import net.sophiebun.buntsy.entity.clockwork_maiden.MaidenInteractionConfig;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockworkMaidenTerminalParticipantScreen extends AbstractContainerScreen<ClockworkCrafterMenu> {

    public static final int MAX_CHANNELS = 16;
    private int channelEdit = 1;
    private Button channelPanel;

    private boolean editingInsert = true;
    private Button insertExtractPanel;

    private final List<Button> sideConfigButtons = new ArrayList<>();

    private Button enableDisableButton;

    private Button priorityPanel;
    private final List<Button> priorityConfigButtons = new ArrayList<>();

    private Button whiteListButton;
    private Button setExactButton;

    private int modifier = 1;

    private final BlockPos pos;

    private final CMTParticipantData data;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BuntsyMod.MODID, "textures/gui/clockwork_crafter_gui.png");

    public ClockworkMaidenTerminalParticipantScreen(ClockworkCrafterMenu pMenu, Inventory pPlayerInventory, Component pTitle, BlockPos pos, CMTParticipantData data) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 205;

        this.pos = pos;
        this.data = data;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 37;
        this.inventoryLabelY = 116;

        addChannelPanel();
        addInsertExtractEdit();
        addPriorityButtons();
        addSideConfigButtons();
        addToggleButton();
        addWhiteListButtons();
    }

    private void changeChannel(int amount){

        saveCurrentWhiteList();
        channelEdit += amount;
        if (channelEdit < 1){
            channelEdit = MAX_CHANNELS;
        } else if (channelEdit > MAX_CHANNELS){
            channelEdit = 1;
        }
        loadWhiteList();
    }

    private void addChannelPanel(){

        this.addRenderableWidget(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changeChannel(-1);
                        })
                .bounds(6, 13, 6, 13)
                .build());

        this.channelPanel = Button.builder(
                        Component.literal("Channel 1"), pButton -> {})
                .bounds(14, 13, 40, 13)
                .build();

        this.channelPanel.active = false;
        this.addRenderableWidget(this.channelPanel);

        this.addRenderableWidget(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changeChannel(1);
                        })
                .bounds(56, 13, 6, 13)
                .build());
    }

    private void updateChannelPanel(){
        this.channelPanel.setMessage(Component.literal("Channel " + this.channelEdit));
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
        this.addRenderableWidget(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changeInsertExtractEdit();
                        })
                .bounds(72, 13, 6, 13)
                .build());

        this.insertExtractPanel = Button.builder(
                        Component.literal("Insert"), pButton -> {
                        })
                .bounds(80, 13, 40, 13)
                .build();

        this.insertExtractPanel.active = false;
        this.addRenderableWidget(this.insertExtractPanel);

        this.addRenderableWidget(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changeInsertExtractEdit();
                        })
                .bounds(122, 13, 6, 13)
                .build());
    }

    private void updateInsertExtractPanel(){
        this.channelPanel.setMessage(Component.literal(this.editingInsert ? "Insert" : "Extract"));
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

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.DOWN);
                        })
                .bounds(6, 92, 20, 20)
                .build());

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.UP);
                        })
                .bounds(52, 46, 20, 20)
                .build());

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.NORTH);
                        })
                .bounds(29, 46, 20, 20)
                .build());

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.SOUTH);
                        })
                .bounds(29, 92, 20, 20)
                .build());

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.WEST);
                        })
                .bounds(6, 69, 20, 20)
                .build());

        this.sideConfigButtons.add(Button.builder(
                        Component.literal(""), pButton -> {
                            setSide( Direction.EAST);
                        })
                .bounds(52, 69, 20, 20)
                .build());

        for (Button button : this.sideConfigButtons){
            this.addRenderableWidget(button);
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
            for (Button button : this.sideConfigButtons){
                button.visible = false;
            }
        }
    }

    private void renderSideConfigButtonsIcons(GuiGraphics guiGraphics){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            guiGraphics.blit(TEXTURE, 6, 92, 176, 0, 20, 20);
            guiGraphics.blit(TEXTURE, 52, 46, 176, 20, 20, 20);
            guiGraphics.blit(TEXTURE, 29, 46, 176, 40, 20, 20);
            guiGraphics.blit(TEXTURE, 29, 92, 176, 60, 20, 20);
            guiGraphics.blit(TEXTURE, 6, 69, 176, 80, 20, 20);
            guiGraphics.blit(TEXTURE, 52, 69, 176, 100, 20, 20);
        }
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
        config.setPriority(config.getPriority() + amount);
        setChanged();
    }

    private void addPriorityButtons(){

        this.priorityConfigButtons.add(Button.builder(
                        Component.literal("◀"), pButton -> {
                            changePriority(-modifier);
                        })
                .bounds(6, 30, 6, 13)
                .build());

        this.priorityPanel = Button.builder(
                        Component.literal("0"), pButton -> {})
                .bounds(14, 30, 40, 13)
                .build();

        this.priorityPanel.active = false;
        this.priorityConfigButtons.add(this.priorityPanel);

        this.priorityConfigButtons.add(Button.builder(
                        Component.literal("▶"), pButton -> {
                            changePriority(modifier);
                        })
                .bounds(56, 30, 6, 13)
                .build());

        for (Button button : this.priorityConfigButtons){
            this.addRenderableWidget(button);
        }
    }

    private void updatePriorityButtons(){
        if (this.data.isEnabled(editingInsert, channelEdit)){
            for (int i = 0; i < 6 ; i++){
                this.priorityConfigButtons.get(i).visible = true;
            }
            this.priorityPanel.setMessage(Component.literal( "" + data.getConfig(editingInsert, channelEdit).getPriority()));
        } else {
            for (Button button : this.priorityConfigButtons){
                button.visible = false;
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
                .bounds(152, 59, 18, 18)
                .tooltip(Tooltip.create(Component.literal("Set whitelist")))
                .build();

        this.addRenderableWidget(this.whiteListButton);

        this.setExactButton = Button.builder(
                        Component.literal(""), pButton -> {
                            toggleExact();
                        })
                .bounds(152, 85, 18, 18)
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
                .bounds(91, 30, 65, 13)
                .build();

        this.addRenderableWidget(this.enableDisableButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgress(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        updateChannelPanel();
        updateInsertExtractPanel();
        updateSideConfigButtons();
        updateToggleButton();
        updatePriorityButtons();
        updateWhiteListButtons();

        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderSideConfigButtonsIcons(guiGraphics);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 107, y + 34, 176, 0, (int)(24 * (menu.getProgress() / ((float) menu.getMaxProgress()))), 17);
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
