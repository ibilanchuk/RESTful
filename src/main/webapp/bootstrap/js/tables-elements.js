
var table;
var q=0;

                    
     

// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
//         
$(document).ready(function(){  
$.fn.dataTable.pipeline = function ( opts ){
    // Configuration options 
    var conf = $.extend( {
        pages: 5,     // number of pages to cache
        url: '',      // script url
        data: null,   // function or object with parameters to send to the server
                      // matching how `ajax.data` works in DataTables
        method: 'GET' // Ajax HTTP method
    }, opts );
 
    // Private variables for storing the cache
    var cacheLower = -1;
    var cacheUpper = null;
    var cacheLastRequest = null;
    var cacheLastJson = null;
 
 
    return function ( request, drawCallback, settings ) {
        var ajax          = false;
        var requestStart  = request.start;
        var drawStart     = request.start;
        var requestLength = request.length;
        var requestEnd    = requestStart + requestLength;
         
  
        if ( settings.clearCache ) {
            // API requested that the cache be cleared
            ajax = true;
            settings.clearCache = false;
        }
        else if ( cacheLower < 0 || requestStart < cacheLower || requestEnd > cacheUpper ) {
            // outside cached data - need to make a request
            ajax = true;
        }
        else if ( JSON.stringify( request.order )   !== JSON.stringify( cacheLastRequest.order ) ||
                  JSON.stringify( request.columns ) !== JSON.stringify( cacheLastRequest.columns ) ||
                  JSON.stringify( request.search )  !== JSON.stringify( cacheLastRequest.search )
        ) {
            // properties changed (ordering, columns, searching)
            ajax = true;
        }
         
        // Store the request for checking next time around
        cacheLastRequest = $.extend( true, {}, request );
 
        if ( ajax ) {
            // Need data from the server
            if ( requestStart < cacheLower ) {
                requestStart = requestStart - (requestLength*(conf.pages-1));
 
                if ( requestStart < 0 ) {
                    requestStart = 0;
                }
            }
             
            cacheLower = requestStart;
            cacheUpper = requestStart + (requestLength * conf.pages);
 
            request.start = requestStart;
            request.length = requestLength*conf.pages;
 
            // Provide the same `data` options as DataTables.
            if ( $.isFunction ( conf.data ) ) {
                // As a function it is executed with the data object as an arg
                // for manipulation. If an object is returned, it is used as the
                // data object to submit
                var d = conf.data( request );
                if ( d ) {
                    $.extend( request, d );
                }
            }
            else if ( $.isPlainObject( conf.data ) ) {
                // As an object, the data given extends the default
                $.extend( request, conf.data );
            }
 
            settings.jqXHR = $.ajax( {
                "type":     conf.method,
                "url":      conf.url,
                "data":     request,
                "dataType": "json",
                "cache":    false,
                "success":  function ( json ) {

                    json['recordsTotal'] = json.count[0];
                    json['recordsFiltered'] = json.count[0];
      
                    cacheLastJson = $.extend(true, {}, json);
                  
                    if ( cacheLower != drawStart ) {
                        json.data.splice( 0, drawStart-cacheLower );
                    }
                    json.data.splice( requestLength, json.data.length );
                     
                    drawCallback( json );
                }
            } );
        }
        else {
            json = $.extend( true, {}, cacheLastJson );
            json.draw = request.draw; // Update the echo for each response
            json.data.splice( 0, requestStart-cacheLower );
            json.data.splice( requestLength, json.data.length );
 
            drawCallback(json);
        }
    }
};
$.fn.dataTable.Api.register( 'clearPipeline()', function () {
    return this.iterator( 'table', function ( settings ) {
        settings.clearCache = true;
    } );
} );

 
table = $("#feature_table").DataTable({
             "processing": true,
             "serverSide": true,
              "ajax": $.fn.dataTable.pipeline({
              "url": '../../../../RESTful/api/service/Features',
              "deferRender": true,
              "dataSrc": "",
              "type": "GET",
              "pages": 5
             
        }),
        "columns": [
            
            {"data": "id", 'id':'id', "title":'ID'},
            {"data": "renderingEngine", "title":"Rendering Engine"},
            {"data": "browser", "title":"Browser"},
            {"data": "platform", "title":"Platform"},
            {"data": "engineVersion", "title":"Engine Version"},
            {"data": "cssGrade", "title":"Css Grade"},
            {"targets": -1,"data": null,"defaultContent": "<a id = 'edit' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp<a id = 'delete' class='btn btn-sm btn-danger'><i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}    
        ],
        
       
    
        "ordering": true,
        "paging": true,
    
      "searching": true,
      "info": true,
       "autoWidth": false
      
      }); 


  $('#feature_table tbody').on( 'click', '#delete', function () {
      
        var data = table.row( $(this).parents('tr') ).data();
        var id = data["id"];
        if (window.confirm('You really wanna delete this feature?'))
        {
        deleteFeature(id);
                 table.row( $(this).parents('tr') ).remove().clearPipeline().draw();

       // table.row( $(this).parents('tr') ).remove().draw();
     //   table.clearPipeline().draw();
         // They clicked Yes
        }
        else
        {
    // They clicked no
        }
     
    } );
   var data;
   $('#feature_table tbody').on( 'click', '#edit', function () {
          data = table.row( $(this).parents('tr') ).data();
            $('[name="id"]').val(data["id"]);
            $('[name="renderingEngine"]').val(data["renderingEngine"]);
            $('[name="browser"]').val(data["browser"]);
            $('[name="platform"]').val(data["platform"]);
            $('[name="engineVersion"]').val(data["engineVersion"]);
            $('[name="cssGrade"]').val(data["cssGrade"]);
          $('#modal_form').modal('show');
          $('.modal-title').text('Edit Features');
          save_method = 'update';

    });

    $('#btnSave').click(function(){
    var dataType;
    var url;
    var data = {
        renderingEngine: $('[name="renderingEngine"]').val(),
        browser:$('[name="browser"]').val(),
        platform:$('[name="platform"]').val(),
        engineVersion:$('[name="engineVersion"]').val(),
        cssGrade:$('[name="cssGrade"]').val()
      }
     var jsdata = JSON.stringify(data);   

     if(save_method =='add'){
      $('.modal-title').text('Add Features');
      url = "../../../../RESTful/api/service/Feature";    
      dataType = "POST"; 
     }
     else{
      $('.modal-title').text('Edit Features');  
      url = "../../../../RESTful/api/service/Feature/"+$('[name="id"]').val();   
      dataType = "PUT";
     }
  $('#btnSave').text('saving...'); //change button text
  $('#btnSave').attr('disabled',true); //set button disable
  $.ajax({
        url : url,
        contentType: "application/json",
        data: jsdata,
        type: dataType,
      
        success: function(data)
        {   
           
           
             $('#btnSave').text('save'); //change button text
             $('#btnSave').attr('disabled',false); //set button enable
             $('#modal_form').modal('hide');
                  table.clearPipeline().draw();
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            alert('Error with update or create data');
            $('#btnSave').text('save'); //change button text
            $('#btnSave').attr('disabled',false); //set button enable
        }
     });
    });
     function deleteFeature($id) {
    $.ajax({
        "url": '../../../../RESTful/api/service/Feature/'+$id,
            "deferRender": true, 
            "type": "DELETE"
      
    });
  }

  $('#reload').click(function(){
    table.clearPipeline().draw();
});

    

});



   

 

 

$('#add_feature').click(function(){

    save_method = 'add';
    $('#form')[0].reset(); // reset form on modals
    $('.form-group').removeClass('has-error'); // clear error class
    $('.help-block').empty(); // clear error string
    $('#modal_form').modal('show'); // show bootstrap modal
    $('.modal-title').text('Add Feature'); // Set Title to Bootstrap modal title


});
 



