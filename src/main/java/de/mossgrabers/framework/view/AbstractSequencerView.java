// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.view;

import de.mossgrabers.framework.configuration.Configuration;
import de.mossgrabers.framework.controller.IControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.INoteClip;
import de.mossgrabers.framework.daw.constants.Resolution;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Abstract implementation for a view which provides a sequencer.
 *
 * @param <S> The type of the control surface
 * @param <C> The type of the configuration
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class AbstractSequencerView<S extends IControlSurface<C>, C extends Configuration> extends AbstractView<S, C> implements SceneView
{
    /** The color for highlighting a step with no content. */
    public static final String    COLOR_STEP_HILITE_NO_CONTENT = "COLOR_STEP_HILITE_NO_CONTENT";
    /** The color for highlighting a step with with content. */
    public static final String    COLOR_STEP_HILITE_CONTENT    = "COLOR_STEP_HILITE_CONTENT";
    /** The color for a step with no content. */
    public static final String    COLOR_NO_CONTENT             = "COLOR_NO_CONTENT";
    /** The color for a step with content. */
    public static final String    COLOR_CONTENT                = "COLOR_CONTENT";
    /** The color for a step with content which is not the start of the note. */
    public static final String    COLOR_CONTENT_CONT           = "COLOR_CONTENT_CONT";
    /** The color for a page. */
    public static final String    COLOR_PAGE                   = "COLOR_PAGE";
    /** The color for an active page. */
    public static final String    COLOR_ACTIVE_PAGE            = "COLOR_ACTIVE_PAGE";
    /** The color for a selected page. */
    public static final String    COLOR_SELECTED_PAGE          = "COLOR_SELECTED_PAGE";
    /** The color for resolution off. */
    public static final String    COLOR_RESOLUTION_OFF         = "COLOR_RESOLUTION_OFF";
    /** The color for resolution. */
    public static final String    COLOR_RESOLUTION             = "COLOR_RESOLUTION";
    /** The color for selected resolution. */
    public static final String    COLOR_RESOLUTION_SELECTED    = "COLOR_RESOLUTION_SELECTED";
    /** The color for transposition. */
    public static final String    COLOR_TRANSPOSE              = "COLOR_TRANSPOSE";
    /** The color for selected transposition. */
    public static final String    COLOR_TRANSPOSE_SELECTED     = "COLOR_TRANSPOSE_SELECTED";

    protected int                 numSequencerRows;
    protected int                 selectedResolutionIndex;
    protected final Configuration configuration;

    protected final int           clipRows;
    protected final int           clipCols;

    private boolean               isSequencerActive;


    /**
     * Constructor.
     *
     * @param name The name of the view
     * @param surface The surface
     * @param model The model
     * @param clipRows The rows of the monitored clip
     * @param clipCols The cols of the monitored clip
     */
    public AbstractSequencerView (final String name, final S surface, final IModel model, final int clipRows, final int clipCols)
    {
        this (name, surface, model, clipRows, clipCols, clipRows);
    }


    /**
     * Constructor.
     *
     * @param name The name of the view
     * @param surface The surface
     * @param model The model
     * @param clipRows The rows of the monitored clip
     * @param clipCols The cols of the monitored clip
     * @param numSequencerRows The number of displayed rows of the sequencer
     */
    public AbstractSequencerView (final String name, final S surface, final IModel model, final int clipRows, final int clipCols, final int numSequencerRows)
    {
        super (name, surface, model);

        this.clipRows = clipRows;
        this.clipCols = clipCols;

        this.configuration = this.surface.getConfiguration ();

        this.selectedResolutionIndex = 4;

        this.numSequencerRows = numSequencerRows;

        this.getClip ();
    }


    /** {@inheritDoc} */
    @Override
    public void onActivate ()
    {
        super.onActivate ();

        this.getClip ().setStepLength (Resolution.getValueAt (this.selectedResolutionIndex));
    }


    /** {@inheritDoc} */
    @Override
    public void updateControlSurface ()
    {
        this.isSequencerActive = this.model.canSelectedTrackHoldNotes () && this.getClip ().doesExist ();

        super.updateControlSurface ();
    }


    /** {@inheritDoc} */
    @Override
    public void onScene (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        if (!this.isActive ())
            return;

        this.selectedResolutionIndex = 7 - index;
        final Resolution resolution = Resolution.values ()[this.selectedResolutionIndex];
        this.getClip ().setStepLength (resolution.getValue ());
        this.surface.getDisplay ().notify (resolution.getName ());
    }


    /**
     * Scroll clip left.
     *
     * @param event The event
     */
    public void onLeft (final ButtonEvent event)
    {
        if (event == ButtonEvent.DOWN)
            this.getClip ().scrollStepsPageBackwards ();
    }


    /**
     * Scroll clip right.
     *
     * @param event The event
     */
    public void onRight (final ButtonEvent event)
    {
        if (event == ButtonEvent.DOWN)
            this.getClip ().scrollStepsPageForward ();
    }


    /**
     * Get the clip.
     *
     * @return The clip
     */
    public final INoteClip getClip ()
    {
        return this.model.getNoteClip (this.clipCols, this.clipRows);
    }


    /**
     * Checks if the given number is in the current display.
     *
     * @param x The index to check
     * @return True if it should be displayed
     */
    protected boolean isInXRange (final int x)
    {
        final INoteClip clip = this.getClip ();
        final int stepSize = clip.getNumSteps ();
        final int start = clip.getEditPage () * stepSize;
        return x >= start && x < start + stepSize;
    }


    /**
     * Calculates how many semi-notes are between the first and last 'pad'.
     *
     * @return The number of semi-notes
     */
    protected int getScrollOffset ()
    {
        final int pos = this.numSequencerRows;
        return pos / 7 * 12 + this.keyManager.map (pos % 7) - this.keyManager.map (0);
    }


    /**
     * Calculates the length of one sequencer page which is the number of displayed steps multiplied
     * with the current grid resolution.
     *
     * @param numOfSteps The number of displayed steps
     * @return The floor of the length
     */
    protected int getLengthOfOnePage (final int numOfSteps)
    {
        return (int) Math.floor (numOfSteps * Resolution.getValueAt (this.selectedResolutionIndex));
    }


    /**
     * Get the color for a sequencer page.
     *
     * @param loopStartPage The page where the loop starts
     * @param loopEndPage The page where the loop ends
     * @param playPage The page which contains the currently played step
     * @param selectedPage The page selected fpr editing
     * @param page The page for which to get the color
     * @return The color to use
     */
    protected String getPageColor (final int loopStartPage, final int loopEndPage, final int playPage, final int selectedPage, final int page)
    {
        if (page == playPage)
            return AbstractSequencerView.COLOR_ACTIVE_PAGE;

        if (page == selectedPage)
            return AbstractSequencerView.COLOR_SELECTED_PAGE;

        if (page < loopStartPage || page >= loopEndPage)
            return AbstractSequencerView.COLOR_NO_CONTENT;

        return AbstractSequencerView.COLOR_PAGE;
    }


    /**
     * Check if there is a note clip to edit.
     *
     * @return Returns true if the selected track can hold notes and the selected clip exists
     */
    protected boolean isActive ()
    {
        return this.isSequencerActive;
    }
}