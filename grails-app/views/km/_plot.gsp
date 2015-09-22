<p>&nbsp;</p>
<div class="col-md-3">
	<g:form controller="km" action="index" method="GET" onsubmit="return plotSavedPlots(this);">
		<div class="form-group">
			<g:select name="cohort-name" from="${session.savedSampleCohorts}" optionValue="name" optionKey="name" class="selectpicker"
			 multiple="true" value="${params.list('cohort-name')}" />
		</div>
		<input type="hidden" name="cohort-type" value="saved" />
		<g:submitButton name="submit" value="Submit" class="btn btn-primary"/>
	</g:form>
</div>
<div class="col-md-9">
	<div id="ChartDiv" style="height:500px;"></div>
</div>