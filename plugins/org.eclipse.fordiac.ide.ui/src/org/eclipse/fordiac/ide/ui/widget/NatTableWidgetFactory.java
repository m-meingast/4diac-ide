/*******************************************************************************
 * Copyright (c) 2022 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Sebastian Hollersbacher - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.fordiac.ide.ui.widget;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.action.KeyEditAction;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.command.DeleteSelectionCommandHandler;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.config.DefaultColumnHeaderStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.painter.NatTableBorderOverlayPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionBindings;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.SelectionStyleLabels;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellEditorMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.LetterOrDigitKeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public final class NatTableWidgetFactory {
	public static final String DEFAULT_LABEL = "DEFAULT_LABEL"; //$NON-NLS-1$
	public static final String ERROR_LABEL = "ERROR_LABEL"; //$NON-NLS-1$

	public static NatTable createNatTable(final Composite parent, final DataLayer dataLayer,
			final IDataProvider headerDataProvider, final IEditableRule editableRule) {

		dataLayer.setColumnPercentageSizing(true);

		final SelectionLayer selectionLayer = new SelectionLayer(dataLayer);
		selectionLayer.addConfiguration(new DefaultSelectionBindings() {
			@Override
			public void configureUiBindings(final UiBindingRegistry uiBindingRegistry) {
				super.configureUiBindings(uiBindingRegistry);
				uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.MOD1, 'v'),
						new PasteDataIntoTableAction());
				uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.MOD1, 'x'), new CutDataFromTableAction());
			}
		});
		selectionLayer.registerCommandHandler(new DeleteSelectionCommandHandler(selectionLayer));
		final ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		final DataLayer columnHeaderDataLayer = new DataLayer(headerDataProvider);
		final ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer,
				selectionLayer);

		final CompositeLayer gridLayer = new CompositeLayer(1, 2);
		gridLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
		gridLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);

		gridLayer.addConfiguration(new DefaultEditConfiguration());
		gridLayer.addConfiguration(new AbstractUiBindingConfiguration() {
			@Override
			public void configureUiBindings(final UiBindingRegistry uiBindingRegistry) {
				uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.NONE, SWT.SPACE), new KeyEditAction());
				uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.NONE, SWT.F2), new KeyEditAction());
				uiBindingRegistry.registerKeyBinding(new LetterOrDigitKeyEventMatcher(), new KeyEditAction());
				uiBindingRegistry.registerKeyBinding(new LetterOrDigitKeyEventMatcher(SWT.MOD2), new KeyEditAction());
				uiBindingRegistry.registerDoubleClickBinding(new CellEditorMouseEventMatcher(GridRegion.BODY),
						new MouseEditAction());
			}
		});

		gridLayer.addConfiguration(new AbstractRegistryConfiguration() {
			@Override
			public void configureRegistry(final IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, editableRule);
			}
		});

		final NatTable table = new NatTable(parent, gridLayer, false);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		final DefaultNatTableStyleConfiguration tableStyle = new DefaultNatTableStyleConfiguration();
		final DefaultSelectionStyleConfiguration selectionStyle = new DefaultSelectionStyleConfiguration();
		final DefaultColumnHeaderStyleConfiguration headerStyle = new DefaultColumnHeaderStyleConfiguration();

		tableStyle.bgColor = GUIHelper.COLOR_WHITE;
		tableStyle.cellPainter = new TextPainter();

		selectionStyle.fullySelectedHeaderBgColor = GUIHelper.COLOR_WHITE;
		selectionStyle.selectedHeaderBgColor = GUIHelper.COLOR_WHITE;
		selectionStyle.selectedHeaderFgColor = GUIHelper.COLOR_BLACK;
		selectionStyle.selectedHeaderFont = headerStyle.font;
		selectionStyle.anchorBgColor = GUIHelper.COLOR_TITLE_INACTIVE_BACKGROUND;
		selectionStyle.anchorFgColor = GUIHelper.COLOR_BLACK;

		headerStyle.bgColor = GUIHelper.COLOR_WHITE;
		headerStyle.renderGridLines = Boolean.TRUE;
		headerStyle.cellPainter = new TextPainter();

		table.setBackground(GUIHelper.COLOR_WHITE);
		table.addOverlayPainter(new NatTableBorderOverlayPainter());

		table.addConfiguration(tableStyle);
		selectionLayer.addConfiguration(selectionStyle);
		columnHeaderLayer.addConfiguration(headerStyle);

		table.addConfiguration(new AbstractRegistryConfiguration() {
			@Override
			public void configureRegistry(final IConfigRegistry configRegistry) {
				Style cellStyle = new Style();
				cellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, GUIHelper.COLOR_DARK_GRAY);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
						DEFAULT_LABEL);

				cellStyle = new Style();
				cellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_RED);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
						ERROR_LABEL);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle,
						DisplayMode.SELECT, ERROR_LABEL);
				configRegistry.unregisterConfigAttribute(CellConfigAttributes.CELL_STYLE, DisplayMode.SELECT,
						SelectionStyleLabels.SELECTION_ANCHOR_STYLE);
			}
		});

		table.configure();
		return table;
	}
}