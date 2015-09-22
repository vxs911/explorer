<!-- Modal -->
<div class="modal fade bs-modal-sm in" id="modalMessage" tabindex="-1" role="dialog" aria-labelledby="modalMessageLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="modalMessageLabel">Title</h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
	       	<label>Cohort Name: </label>
	       	<input type="text" class="form-control" name="modal-input-value">       
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" onclick="return true">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="pleaseWaitDialog" role="dialog" aria-labelledby="pleaseWaitDialogLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="pleaseWaitDialogLabel">Please wait</h4>
      </div>
      <div class="modal-body">
      	<div class="progress progress-striped active">
			<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;"></div>
		</div>
      </div>
    </div>
  </div>
</div>