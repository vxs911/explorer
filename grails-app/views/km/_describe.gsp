<g:form role="form" name="set-survival-form" id="set-survival-form" controller="km" onsubmit="return false;" action="setSurvivalPhenotype">				
	<div class="form-group">
		<label for="survival-time-variable-name">Survival Time</label>
		<g:select name="survival-time-variable-name" id="survival-time-variable-name" class="form-control selectpicker" from="${headers}" />							
	</div>
	<div class="form-group">
		<label>Survival Event</label>
		<g:select name="survival-event-variable-name" id="survival-event-variable-name" class="form-control selectpicker" from="${headers}" />
	</div>
	<div class="checkbox">
		<label>Don't ask me again in this session</label>
		<input type="checkbox" name="save-survival-preference" />
	</div>
	<%--
	<div class="form-group">
		<g:submitButton name="submit" value="Continue" class="btn btn-primary" />
	</div>
	 --%>
</g:form>