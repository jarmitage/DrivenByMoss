// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.mode;

import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.controller.display.Format;
import de.mossgrabers.framework.controller.display.ITextDisplay;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.INoteClip;
import de.mossgrabers.framework.graphics.display.DisplayModel;
import de.mossgrabers.framework.scale.Scales;
import de.mossgrabers.framework.utils.StringUtils;


/**
 * Editing of note parameters.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteMode extends BaseMode
{
    INoteClip clip = null;
    int       step = 0;
    int       note = 60;


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public NoteMode (final PushControlSurface surface, final IModel model)
    {
        super ("Note", surface, model);
    }


    /**
     * Set the values.
     *
     * @param clip The clip to edit
     * @param step The step to edit
     * @param note The note to edit
     */
    public void setValues (final INoteClip clip, final int step, final int note)
    {
        this.clip = clip;
        this.step = step;
        this.note = note;
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobValue (final int index, final int value)
    {
        switch (index)
        {
            case 1:
                this.clip.changeStepDuration (this.step, this.note, value);
                break;

            case 2:
                this.clip.changeStepVelocity (this.step, this.note, value);
                break;

            case 3:
                this.clip.changeStepReleaseVelocity (this.step, this.note, value);
                break;

            case 4:
                this.clip.changeStepPressure (this.step, this.note, value);
                break;

            case 5:
                this.clip.changeStepTimbre (this.step, this.note, value);
                break;

            case 6:
                this.clip.changeStepPan (this.step, this.note, value);
                break;

            case 7:
                this.clip.changeStepTranspose (this.step, this.note, value);
                break;

            default:
                return;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 (final ITextDisplay display)
    {
        final double noteVelocity = this.clip.getStepVelocity (this.step, this.note);
        final double noteReleaseVelocity = this.clip.getStepReleaseVelocity (this.step, this.note);
        final double notePressure = this.clip.getStepPressure (this.step, this.note);
        final double noteTimbre = this.clip.getStepTimbre (this.step, this.note);
        final double notePan = this.clip.getStepPan (this.step, this.note);
        final double noteTranspose = this.clip.getStepTranspose (this.step, this.note);

        final IValueChanger valueChanger = this.model.getValueChanger ();
        final int parameterValue = valueChanger.fromNormalizedValue (noteVelocity);
        final int parameterReleaseValue = valueChanger.fromNormalizedValue (noteReleaseVelocity);
        final int parameterPressureValue = valueChanger.fromNormalizedValue (notePressure);
        final int parameterTimbreValue = valueChanger.fromNormalizedValue ((noteTimbre + 1.0) / 2.0);
        final int parameterPanValue = valueChanger.fromNormalizedValue ((notePan + 1.0) / 2.0);
        final int parameterTransposeValue = valueChanger.fromNormalizedValue ((noteTranspose + 24.0) / 48.0);

        display.setCell (0, 1, "Length").setCell (1, 1, this.formatLength ());
        display.setCell (0, 2, "Velocity").setCell (1, 2, formatPercentage (noteVelocity)).setCell (2, 2, parameterValue, Format.FORMAT_VALUE);
        display.setCell (0, 3, "R-Velocity").setCell (1, 3, formatPercentage (noteReleaseVelocity)).setCell (2, 3, parameterReleaseValue, Format.FORMAT_VALUE);
        display.setCell (0, 4, "Pressure").setCell (1, 4, formatPercentage (notePressure)).setCell (2, 4, parameterPressureValue, Format.FORMAT_VALUE);
        display.setCell (0, 5, "Timbre").setCell (1, 5, formatPercentage (noteTimbre)).setCell (2, 5, parameterTimbreValue, Format.FORMAT_VALUE);
        display.setCell (0, 6, "Pan").setCell (1, 6, formatPercentage (notePan)).setCell (2, 6, parameterPanValue, Format.FORMAT_PAN);
        display.setCell (0, 7, "Pitch").setCell (1, 7, String.format ("%.1f", Double.valueOf (noteTranspose))).setCell (2, 7, parameterTransposeValue, Format.FORMAT_PAN);

        display.setCell (3, 0, "Step: " + (this.step + 1));
        display.setCell (3, 1, "Note: " + Scales.formatNoteAndOctave (this.note, -3));
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 (final DisplayModel message)
    {
        final double noteVelocity = this.clip.getStepVelocity (this.step, this.note);
        final double noteReleaseVelocity = this.clip.getStepReleaseVelocity (this.step, this.note);
        final double notePressure = this.clip.getStepPressure (this.step, this.note);
        final double noteTimbre = this.clip.getStepTimbre (this.step, this.note);
        final double notePan = this.clip.getStepPan (this.step, this.note);
        final double noteTranspose = this.clip.getStepTranspose (this.step, this.note);

        final IValueChanger valueChanger = this.model.getValueChanger ();
        final int parameterValue = valueChanger.fromNormalizedValue (noteVelocity);
        final int parameterReleaseValue = valueChanger.fromNormalizedValue (noteReleaseVelocity);
        final int parameterPressureValue = valueChanger.fromNormalizedValue (notePressure);
        final int parameterTimbreValue = valueChanger.fromNormalizedValue ((noteTimbre + 1.0) / 2.0);
        final int parameterPanValue = valueChanger.fromNormalizedValue ((notePan + 1.0) / 2.0);
        final int parameterTransposeValue = valueChanger.fromNormalizedValue ((noteTranspose + 24.0) / 48.0);

        message.addOptionElement ("Step: " + (this.step + 1), "", false, "Note: " + Scales.formatNoteAndOctave (this.note, -3), "", false, false);

        message.addParameterElement ("Length", -1, this.formatLength (), this.isKnobTouched[1], -1);
        message.addParameterElement ("Velocity", parameterValue, formatPercentage (noteVelocity), this.isKnobTouched[2], parameterValue);
        message.addParameterElement ("R-Velocity", parameterReleaseValue, formatPercentage (noteReleaseVelocity), this.isKnobTouched[3], parameterReleaseValue);
        message.addParameterElement ("Pressure", parameterPressureValue, formatPercentage (notePressure), this.isKnobTouched[4], parameterPressureValue);
        message.addParameterElement ("Timbre", parameterTimbreValue, formatPercentage (noteTimbre), this.isKnobTouched[5], parameterTimbreValue);
        message.addParameterElement ("Pan", parameterPanValue, formatPercentage (notePan), this.isKnobTouched[6], parameterPanValue);
        message.addParameterElement ("Pitch", parameterTransposeValue, String.format ("%.1f", Double.valueOf (noteTranspose)), this.isKnobTouched[7], parameterTransposeValue);
    }


    /**
     * Format a velocity percentage.
     * 
     * @param noteVelocity The velocity in the range of 0..1.
     * @return The formatted velocity
     */
    private static String formatPercentage (final double noteVelocity)
    {
        return String.format ("%.01f%%", Double.valueOf (noteVelocity * 100.0));
    }


    /**
     * Format the duration of the current note.
     *
     * @return The formatted value
     */
    private String formatLength ()
    {
        final double noteLength = this.clip.getStepDuration (this.step, this.note);
        return StringUtils.formatMeasuresLong (this.model.getTransport ().getQuartersPerMeasure (), noteLength, 0);
    }
}