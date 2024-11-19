package org.remast.baralga.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jdesktop.swingx.JXHeader;
import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.gui.settings.UserSettings;
import org.remast.swing.dialog.EscapeDialog;
import org.remast.swing.util.LabeledItem;
import org.remast.util.TextResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The settings dialog for editing both application and user settings.
 * 
 * @author remast
 */
@SuppressWarnings("serial")
public class SettingsDialog extends EscapeDialog implements ActionListener {

	/** The logger. */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SettingsDialog.class);

	/** The bundle for internationalized texts. */
	private static final TextResourceBundle textBundle = TextResourceBundle.getBundle(SettingsDialog.class);

	/** Component to edit setting to remember window size and location. */
	private JCheckBox rememberWindowSizeLocation;

	/** Component to edit setting to show stopwatch. */
	private JCheckBox showStopwatch;

    /** Component to edit setting for duration format. */
    private JComboBox<LabeledItem<UserSettings.DurationFormat>> durationFormatSelector;

	/** The model. */
	private final PresentationModel model;

    private static final LabeledItem<UserSettings.DurationFormat> [] DURATION_FORMAT_OPTIONS = new LabeledItem [] {
            new LabeledItem<>(UserSettings.DurationFormat.DECIMAL, textBundle.textFor("SettingsDialog.DurationFormat.DECIMAL")),
            new LabeledItem<>(UserSettings.DurationFormat.HOURS_AND_MINUTES, textBundle.textFor("SettingsDialog.DurationFormat.HOURS_AND_MINUTES"))
    };

	/**
	 * Creates a new settings dialog.
	 * 
	 * @param owner
	 *            the owning frame
	 * @param model
	 *            the model
	 */
	public SettingsDialog(final Frame owner, final PresentationModel model) {
		super(owner);
		this.model = model;

		initialize();
	}

	/**
	 * Set up GUI components.
	 */
	private void initialize() {
		setLocationRelativeTo(getOwner());

		final double border = 5;
		final double[][] size = {
                { border, TableLayout.PREFERRED, border, TableLayout.FILL, border }, // Columns
				{ border, TableLayout.PREFERRED, border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.PREFERRED, border } // Rows
		};

		final TableLayout tableLayout = new TableLayout(size);
		this.setLayout(tableLayout);

		this.setSize(400, 180);

		this.setName("SettingsDialog"); //$NON-NLS-1$
		this.setTitle(textBundle.textFor("SettingsDialog.Title")); //$NON-NLS-1$
		final ImageIcon icon = new ImageIcon(getClass().getResource("/icons/stock_folder-properties.png")); //$NON-NLS-1$
		this.add(new JXHeader(textBundle.textFor("SettingsDialog.UserSettingsTitle"), null, icon), "0, 0, 3, 1"); //$NON-NLS-1$ //$NON-NLS-2$

		rememberWindowSizeLocation = new JCheckBox(textBundle.textFor("SettingsDialog.Setting.RememberWindowSizeLocation.Title")); //$NON-NLS-1$
		rememberWindowSizeLocation.setToolTipText(textBundle.textFor("SettingsDialog.Setting.RememberWindowSizeLocation.ToolTipText")); //$NON-NLS-1$
		rememberWindowSizeLocation.addActionListener(this);
		this.add(rememberWindowSizeLocation, "1, 3, 3, 3"); //$NON-NLS-1$

		showStopwatch = new JCheckBox(textBundle.textFor("SettingsDialog.ShowStopwatch.Title")); //$NON-NLS-1$
		showStopwatch.setToolTipText(textBundle.textFor("SettingsDialog.ShowStopwatch.ToolTipText")); //$NON-NLS-1$
		showStopwatch.addActionListener(this);
		showStopwatch.addActionListener(e -> model.changeStopWatchVisibility());

		this.add(showStopwatch, "1, 5, 1, 1"); //$NON-NLS-1$

        durationFormatSelector = new JComboBox<>(DURATION_FORMAT_OPTIONS);
        durationFormatSelector.addActionListener(this);
        durationFormatSelector.addActionListener(e -> SettingsDialog.this.model.fireDurationFormatChanged());
        this.add(new JLabel(textBundle.textFor("SettingsDialog.DurationFormat.Label")), "1, 7, 1, 1"); //$NON-NLS-1$
        this.add(durationFormatSelector, "3, 7, 3, 5"); //$NON-NLS-1$

		final JButton resetButton = new JButton(textBundle.textFor("SettingsDialog.ResetButton.Title")); //$NON-NLS-1$
		resetButton.setToolTipText(textBundle.textFor("SettingsDialog.ResetButton.ToolTipText")); //$NON-NLS-1$
		resetButton.addActionListener(e -> resetSettings());

		this.add(resetButton, "1, 9, 3, 5"); //$NON-NLS-1$

		readFromModel();
	}

	/**
	 * Resets all settings to default values.
	 */
	private void resetSettings() {
		UserSettings.instance().reset();
		readFromModel();
	}

	/**
	 * Reads the data displayed in the dialog from the model.
	 */
	private void readFromModel() {
		rememberWindowSizeLocation.setSelected(UserSettings.instance().isRememberWindowSizeLocation());
		showStopwatch.setSelected(UserSettings.instance().isShowStopwatch());

        final UserSettings.DurationFormat durationFormat = UserSettings.instance().getDurationFormat();
        for (LabeledItem<UserSettings.DurationFormat> durationFormatOption : DURATION_FORMAT_OPTIONS) {
            if (durationFormatOption.getItem().equals(durationFormat)) {
                durationFormatSelector.setSelectedItem(durationFormatOption);
                break;
            }
        }
	}

	/**
	 * Writes the data displayed in the dialog to the model.
	 */
	private void writeToModel() {
		UserSettings.instance().setRememberWindowSizeLocation(rememberWindowSizeLocation.isSelected());
		UserSettings.instance().setShowStopwatch(showStopwatch.isSelected());
        UserSettings.instance().setDurationFormat(((LabeledItem<UserSettings.DurationFormat>) durationFormatSelector.getSelectedItem()).getItem());
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		writeToModel();
	}

}
