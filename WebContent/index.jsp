<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <%@page import="pureclass.Publish_App"%>
         <%@page import="java.io.File"%>
    <%
    Publish_App pa = new Publish_App();
    String uploadPath = getServletContext().getRealPath("") + "uploads/";
    File uploadDir = new File(uploadPath);
    uploadPath = uploadDir.getAbsolutePath() + "/files/";
    uploadDir = new File(uploadPath);
    pa.deleteDir(uploadDir);
	%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

<title>MDM Video Encryption</title>
<link rel="stylesheet" type="text/css" href="dataTables.jqueryui.min.css">
<link rel="stylesheet" type="text/css" href="jquery-ui.css">

<script type="text/javascript" src="jquery-3.3.1.js"></script>
<script type="text/javascript" src="jquery.dataTables.min.js"></script>
<script type="text/javascript" src="dataTables.jqueryui.min.js"></script>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
  
</head>
<body>
<!-- Modal -->
		<!-- <div class="modal fade" id="uploadApp" role="dialog">
		    <div class="modal-dialog">
		    	<form method="post" action="PublishAppServlet" enctype = "multipart/form-data">
			      Modal content
			      <div class="modal-content">
			      	
				        <div class="modal-header" style="background:red">
				          <button type="button" style="color:white !important;" class="close" onclick="closeModel()">&times;</button>
				          <h4 class="modal-title textlightblackcolor" style="color:white !important;">Upload Private App
				         
				          </h4>
				        </div>
				        <div class="modal-body">
				          
		                    <div class="box-body">
								 <input type="file" name="file"  id="excelfile" class="textlightblackcolor" >
							  
		                    </div>/.box-body				                    
				                  
				        </div>
				        <div class="modal-footer">			
				        	  <div class="form-group alert alert-info" style="text-align:left;font-size: 13px;font-weight:bold;">
						         	<label class="textlightblackcolor"  for="exampleInputEmail1">Note :- Private apps aren't available outside the enterprise</label>
						       </div>		        	  
				        	  <button type="submit" id="uploadBtn"  class="textlightblackcolor btn btn-primary" >Submit</button>	  	
				        </div>
			         
			      </div>
		     	</form>
		    </div>
		</div> -->
		
		<div class="modal-footer" style='display:none;'>			
        	  
			<h3>Read From Server Folder</h3>
			
			<h5>Enter Folder Name</h5>
			<input type="text" id="foldername">
			<h5 id="message"></h5>
        	<button type="submit" id="uploadBtn" onclick="readFromServerFolder()" class="textlightblackcolor btn btn-primary" >Submit</button>	  	
        </div>
		<div class="" style='border-bottom:1px solid lightgray;padding:15px;'>	
					
			<h3 style='margin: auto;margin-left:0px !important;'><img style='margin-right:15px;width: 55px;height: 55px;' src="client_logo.png">Video Encryption</h3>
        </div>
		<div class="form-group" style='padding:15px;' id="hideUploader">			
        	 <label for="sel1" style='width:100%;'>Select folder</label>
			 <input id="myInput" style='margin-bottom:10px;' class='form-control-file border' type="file" webkitdirectory directory/>					  
        	 <button style='float:right;margin-right:10px !important;' type="submit" id="uploadBtn" onclick="uploadFileOnServer()" class="textlightblackcolor btn btn-primary" >Submit</button>	  	
        </div>
        <div class="form-group" style='padding:15px;display:none;' id="showUploader">		
        		
        		<img id="loaderIssue" style='margin-right:15px;width: 15px;height: 15px;' src="ajax-loader.gif">
        		<a  onclick=" return confirm('Are you sure you want to reload page')" class="btn btn-primary" href="javascript:window.location.href=window.location.href">Back to Dashboard </a>
        </div>
        <br/>
		
		
		<table id="example" class="display" style="width:100%">
	        <thead>
	            <tr>
	                <th>File Name</th>
	                <th>Size</th>
	                <th>Type</th>
	                <th>Status</th>
	                <th>Action</th>
	            </tr>
	        </thead>
	        <tbody id="element">
	           
	        
	           
	        </tbody>        
    </table>
		<script>
			$(document).ready(function() {
			    $('#example').DataTable();
			} );
			
			
			var folderName = "";
			var folder = document.getElementById("myInput");
			folder.onchange=function(){
				console.error(folder);
			  var files = folder.files,
			      len = files.length,
			      i;

			  var t = $('#example').DataTable();
			  for(i=0;i<len;i+=1){
			    console.log(files[i]);
			    if(files[i].type.indexOf("audio") >-1 || files[i].type.indexOf("video") > -1){
			    	
			    	var relativePath = files[i].webkitRelativePath;
			    	folderName = relativePath.split("/")[0];
			    	 var _size = files[i].size;
			    	 var fSExt = new Array('Bytes', 'KB', 'MB', 'GB'),
			         l=0;while(_size>900){_size/=1024;l++;}
			         var exactSize = (Math.round(_size*100)/100)+' '+fSExt[l];
				    	t.row.add( [
				    		files[i].name,
				    		exactSize,
				    		files[i].type,
				            "<h5 style='color:red;font-size: 15px;'>Not Started</h5>",
				            "Click here to submit button"
				           
				        ] ).draw( false );
			    }	
			    
			  }
			  
			}
			
			var _counter = 0;		
			
			function uploadFileOnServer(){
				
				  var files = folder.files, len = files.length;
				  var _c = confirm("Are you sure you want to upload this folder");
				  var _finalArr = [];
				  if(_c){
					  for(i=0;i<len;i+=1){
					    console.log(files[i]);
					    if(files[i].type.indexOf("video") > -1 || files[i].type.indexOf("audio") > -1){
					    	_finalArr.push(files[i]);
					    }	
					    
					  }
				  }
				  
				  if(_finalArr.length == 0){
					  alert("Please select folder first");
					  return;
				  }
				  
				  $('#hideUploader').hide();
				  $('#showUploader').show();
				 
				  _counter = 0;	
				  var table3 = $('#example').DataTable();
	        		table3.rows().every(function (index, element) {
      	            var data = this.data();
      	            if(data[0] == files[_counter].name){
	            			data[3] = "<h5 style='color:orange;font-size: 15px;'>In Progress</h5>";
	                        this.invalidate()
	            		}
	        		});
	        		
				  sendResultToServer(_counter,_finalArr.length,_finalArr);
				 
			}
			
			function sendResultToServer(_counter,len,files){
				
				  
	        		
				 if(_counter == (len)){
					 alert("finished");
				 }else{
					 
					 var formData = new FormData();
					 formData.append("file", files[_counter]);
					 formData.append("folderName", folderName);
					 formData.append("action", "Upload");
					 
					 $.ajax({
					        type: "POST",
					        url: "PublishAppServlet",
					        
					        success: function (response) {
					           console.error(response);
					           
					            var _prevInd = _counter;
					            var table3 = $('#example').DataTable();
				        		table3.rows().every(function (index, element) {
			        	            var data = this.data();
			        	            if(data[0] == files[_prevInd].name){
	        	            			data[3] = "<h5 style='color:green;font-size: 15px;'>Completed</h5>";
	        	            			data[4] = response;
	        	                        this.invalidate()
	        	            		}
	        	        		});
				        		_counter ++;
				        		if(_counter == len){
				        			$('#loaderIssue').hide();
				        		}else{
				        	   		 var table3 = $('#example').DataTable();
					        		table3.rows().every(function (index, element) {
				        	            var data = this.data();
				        	            if(data[0] == files[_counter].name){
		        	            			data[3] = "<h5 style='color:orange;font-size: 15px;'>In Progress</h5>";
		        	                        this.invalidate()
		        	            		}
		        	        		});
						        			
				        		}
				     
	        		 			table3.draw();
					            
					        	 if(_counter == (len)){					        		 
					        		 alert("finished");
					        	 }else{
					        		 sendResultToServer(_counter,len,files);
					        	 }
					            
					        },
					        error: function (error) {
					            // handle error
					        	alert("Error");
					        },
					        //sync: true,
					        data: formData,
					       // dataType: 'json',
					        encType : "multipart/form-data",
					        cache : false,
							processData : false,
							contentType : false
					        //cache: false,
					        //contentType: false,
					        //processData: false,
					       // timeout: 60000
					    });
				 }
				
			}
			
			function readFromServerFolder(){
				
				 var formData = new FormData();
				 formData.append("folderName",  document.getElementById("foldername").value);
				 formData.append("action", "FromServerFolder");
				 document.getElementById("message").innerHTML="In Progress";
				 $.ajax({
				        type: "POST",
				        url: "PublishAppServlet",
				        
				        success: function (data) {
				            // your callback here
				            //alert("2");
				            document.getElementById("message").innerHTML="";
				           console.errr(data);
				            
				        },
				        error: function (error) {
				            // handle error
				        	alert(error);
				        	document.getElementById("message").innerHTML="";
				        },
				        sync: true,
				        data: formData,
				        dataType: 'json',
				        cache: false,
				        contentType: false,
				        processData: false,
				       // timeout: 60000
				    });
				
			}
		</script>
</body>
</html>