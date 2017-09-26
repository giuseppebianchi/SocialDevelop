var flagUpdate = false;
var updatedElement;


$( "#submitsearch" ).click(function() {
    $(this).closest('#findByKeyWord').submit();
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        
        reader.onload = function (e) {
            $('#img-profilo')
                    .attr('src', e.target.result);
            
        };
        
        reader.readAsDataURL(input.files[0]);
    }
}

function remove_skill(param){
    var skill = $.trim($(param).parent().parent().children("a.skill-name").text());
    var userid = $("#userid").val();
    var rm = $(param).parent().parent("li");
    $.ajax({
        datatype:'text/plain',
        type: 'post',
        url: 'rmSkill',
        data: {
            skill:skill,
            userid:userid
        },
        success: function(response) {
            if(response==1){
                rm.remove();
            }
        }
    });              
}


$(".add-skill-btn").on("click",function(){
    var skill = $("#select-skill option:selected");
    var level = $("#select-level option:selected").val();
    var skill_level = skill.val()+":"+level;
    var userid = $("#userid").val();
    var skill_text = skill.text();
    if(skill_text.indexOf("-")>=0){
        skill_text = skill_text.split("-")[1];
    }
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'addSkill',
        data: {
            skill_level:skill_level,
            userid:userid
        },
        success: function (response) {
            if(response == 1){
                $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a><a id='skill-level'>"+level+"</a>\n\
             <span><button onclick='remove_skill(this)' type='button' id='rm-skill-btn' class='btn btn-default btn-sm remove-skill-button rm-skill-btn'><span class='glyphicon glyphicon-remove'></span></button></span></li>");   
            }
        }
    });
    
});


$(".add-skill-to-task-btn").on("click",function(){  
    //se non ci sono skill nella lista annulliamo il valore dell'input nascosto
    if( $('a.skill-name').length == 0){
        $(".input-skills").val(""); 
    }
    var skill = $("#select-skill2 option:selected");
    var level = $("#select-level2 option:selected").val();
    //controlliamo innanzitutto se è stata selezionata una skill e un level
    if(skill.val()!="" && level!=""){
        var skill_level = skill.val()+":"+level;
        var skill_text = skill.text();
        
        if(skill_text.indexOf("-")>=0){
            skill_text = skill_text.split("-")[1];
        }
        //var input_skills = $(".input-skills").val();
        var flag = false;
        //controlliamo se la skill selezionata è già presente nella lista
        var check =  $('a.skill-name:contains("'+skill_text+'")').length;
        if( check > 0){
            flag = true;
        }
        //se non lo è aggiungiamo sia all'input nascosto che alla lista
        if(!flag){
            $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a>\n\
            <a id='skill-level'>"+level+"</a>\n\
            <span><button onclick='remove_skill_from_task(this)' \n\
            type='button' id='rm-skill-from-task-btn' class='btn btn-default btn-sm \n\
            remove-skill-from-task-button rm-skill-from-task-btn'><span class='glyphicon\n\
             glyphicon-remove'></span></button></span></li>");   
        }     
    }
});



function remove_skill_from_task(param){
    var skill = $.trim($(param).parent().parent().children("a.skill-name").text());
    var rm = $(param).parent().parent("li");
    rm.remove();
}          


function reset_popup(){
    $(".list-skill").empty();
    $(".add-task-form")[0].reset();
}


$("a.add-task").on("click", function(){
    $("#popup1").removeClass("hidden");
    //$("#popup1").css("display","block")
    reset_popup();
}); 


function remove_task(param){
    $(param).parent("div").parent("li").remove();
}


function reload_task(name,type,state,start,end,descr,coll,skills){
    $("#task_name").val(name);
   
    
    $("#task_type option").filter(function() {
        return this.text == type; 
    }).attr('selected', true);
    
    
    
    if(isOpen=="Open"){
        $("#isOpen").val(1);
    }else{
        $("#isOpen").val(0);
    }
    $("#start_date").val(start);
    $("#end_date").val(end);
    $("#task_descr").val(descr);
    $("#num_collaborators").val(coll);
    
    var i=0;
    var skl = skills.split(";");
    for(i;i<skl.length-1;i++){
        var splitted = skl[i].split("(");
        var skill_text = splitted[0];
        var level = splitted[1].split(")")[0];
        $(".list-skill").prepend("<li><a class='skill-name' >"+skill_text+"&nbsp;</a>\n\
            <a id='skill-level'>"+level+"</a>\n\
            <span><button onclick='remove_skill_from_task(this)' \n\
            type='button' id='rm-skill-from-task-btn' class='btn btn-default btn-sm \n\
            remove-skill-from-task-button rm-skill-from-task-btn'><span class='glyphicon\n\
             glyphicon-remove'></span></button></span></li>");   
    }
    //cancellare vecchio li solo quando si clicca submit e non nella funzione update_task!!!
    
}



function close_task(param){
    var isOpen = $(param).parent().children("#isOpen");
    if(isOpen.text()=="State: Open"){
        isOpen.text("State: Close");
        $(param).attr('value', 'Open Task');
    }else{
        isOpen.text("State: Open");
        $(param).attr('value', 'Close Task');
    }
    
}


function update_task(param){
    flagUpdate = true;
    var name = $.trim($(param).siblings("p#name").text().split(":")[1]);
    var type = $.trim($(param).siblings("p#tipo").text().split(":")[1]);
    var state = $.trim($(param).siblings("p#isOpen").text().split(":")[1]);
    var start = $.trim($(param).siblings("p#start-end").text().split(":")[1].split("-")[0]);
    var end = $.trim($(param).siblings("p#start-end").text().split("-")[1].split(":")[1]);
    var descr = $.trim($(param).siblings("p#descr").text().split(":")[1]);
    var coll = $.trim($(param).siblings("p#coll").text().split(":")[1]);
    var skills = $.trim($(param).siblings("p.skills").text().split(":")[1]);
    updatedElement = $(param).parent("div").parent("li");
    $("a.add-task").trigger("click");
    document.location.href = $("a.add-task").attr("href");
    reload_task(name,type,state,start,end,descr,coll,skills);
}

function aggiungi_task(name,start,end,descr,ncoll, skill_level, tipo,isOpen){
    if(flagUpdate==true){
        updatedElement.remove();
        flagUpdate=false;
    }
    if(isOpen==1){
        isOpen = "Open";
    }else{
        isOpen = "Close";
    }
    
    $(".task-aggiunti").append("<li><div class='gt-text grade-padding'>\n\
                                <p id='name'>Task: "+name+"</p>\n\
                                <p id='tipo'>Type: "+tipo+"</p>\n\
                                <p id='isOpen'>State: "+isOpen+"</p>\n\
                                <p id='start-end'>Start Date: "+start+" &nbsp; - &nbsp; End Date: "+end+"</p>\n\
                                <p id='descr'>Description: "+descr+"</p>\n\
                                <p id='coll'>Collaborators: "+ncoll+"</p>\n\
                                <p id='append_here_"+name+"' class='skills'>Skills Richieste: </p>\n\
                                <input type='button' value='Update' id='update-skill' onclick='update_task(this)'/>\n\
                                <input type='button' value='Close Task' id='close-skill' onclick='close_task(this)'/></div></li>");
    var i = 0;
    for(i;i<skill_level.length;i++){
        var skill_name = skill_level[i].split(":")[0];
        var level = skill_level[i].split(":")[1];
        $("#append_here_"+name).append(skill_name+"("+level+") ; ");
    }
    
}



$("#task_type").on("change", function(){
    $("#select-skill2 option").removeClass("hidden");
    $(".list-skill").empty();
    $("#select-skill2").val("");
    var type_id = $("#task_type option:selected").val();
    $("#select-skill2 option").not("."+type_id).addClass("hidden");
});


$(function(){
    $(".add-project-form")[0].reset();
    $(".add-task-form")[0].reset();
    
    $(".close-task").each(function(){
        var param = $(this);
       close_task(param); 
    });
});


window.onbeforeunload = function() {
    $(".add-project-form")[0].reset();
    $(".add-task-form")[0].reset();
};


$(".private-check").on("click", function(){
    var private = $(this).parent().siblings(".isPrivate");
    var value = private.val(); 
    if(value==0){
        private.val(1);
    }else{
        private.val(0);
    }
});

$(".open-check").on("click", function(){
    var private = $(this).parent().siblings(".isOpen");
    var value = private.val(); 
    if(value==0){
        private.val(1);
    }else{
        private.val(0);
    }
});



$(".skillSelect").on("click",function(){
    $(this).removeClass("req-list"); 
});

$(".levelSelect").on("click",function(){
    $(this).removeClass("req-list"); 
});

$(".findDeveloper").on("click",function(event){
    var skill_name = $(".skillSelect").val();
    var skill_level = $(".levelSelect").val();
    if(skill_name == "")
    {
        event.preventDefault();
        $(".skillSelect").addClass("req-list");
        $(".skillSelect").focus(); 
    }
    else
    {
        if(skill_level == "")
        {
            event.preventDefault();
            $(".levelSelect").addClass("req-list");
            $(".levelSelect").focus();
        }
    }
    
});


function remove_collaborator(dev_key, task_key){
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'removeCollaboratorFromTask',
        data: {
            dev_key:dev_key,
            task_key:task_key
        },
        success: function (response) {
            if(response == 1){
                $("li#"+dev_key).remove();
            }else{
                
            }
        }
    });
}


function release_vote(dev_key, task_key, param){
    var vote = $(param).parent().parent("span").children(".div_vote").children("select.vote").val();
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'releaseVote',
        data: {
            dev_key:dev_key,
            task_key:task_key,
            vote:vote
        },
        success: function (response) {
            if(response >= 0){
                var here = $(param).parent().parent();
                $(here).empty();
                $(here).append("<h4>"+response+"<i class='fa fa-star'></i></h4>");
            }else{
                //il livello non è stato settato!
            }
        }
    });
}



function join_task(task_key, param){
    
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'joinTask',
        data: {
            task_key:task_key
        },
        success: function (response) {
            $(param).siblings(".join-msg").text(response);
            $(param).siblings(".join-msg").removeClass("hidden");
            setTimeout(function(){ $(param).siblings(".join-msg").addClass("hidden"); }, 5000);
        },
        error: function (){
            $(param).siblings(".join-msg").text("There is already a waiting request");
            $(param).siblings(".join-msg").removeClass("hidden");
            setTimeout(function(){ $(param).siblings(".join-msg").addClass("hidden"); }, 5000);
        }
    });
    
}

function remove_type(param){
    var type = $.trim($(param).parent().parent().children("a.type-name").text());
    
    var rm = $(param).parent().parent("li");
    $.ajax({
        datatype:'text/plain',
        type: 'post',
        url: 'rmType',
        data: {
            type:type
        },
        success: function(response) {
            if(response==1){
                rm.remove();
            }
        }
    });              
}



function send_request(task_key, dev_key, param){
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'sendRequest',
        data: {
            task_key:task_key,
            dev_key:dev_key
        },
        success: function (response) {
            $(param).siblings(".join-msg").removeClass("hidden");
            $(param).siblings(".join-msg").text(response);
            setTimeout(function(){ $(param).siblings(".join-msg").addClass("hidden"); }, 5000);
            
        },
        error: function(){
            $(param).siblings(".join-msg").removeClass("hidden");
            $(param).siblings(".join-msg").text("There is already a waiting request");
            setTimeout(function(){ $(param).siblings(".join-msg").addClass("hidden"); }, 5000);
        }
    });
    
}



function send_request_dft(task_key, dev_key, param){
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'sendRequest',
        data: {
            task_key:task_key,
            dev_key:dev_key
        },
        success: function (response) {
            
            var div = $(param).parent();
            $(param).remove();
            div.append("<p class='center'>Your Request Has Been Sended!</p>");
            
            
        },
        error: function(){
            var button = $(param);
            var div = $(param).parent();
            $(param).remove();
            div.append("<p class='center'>Something went wrong...</p>");
            setTimeout(function(){ div.empty();
                div.append(button);}, 5000);
            
        }
    });
    
}




$(".accept-proposal").on("click",function(){
    var button = $(this);
    button.prop('disabled', true);
    var task = $(this).parent().parent().parent();
    var task_key = task.attr("id"); 
    var sender = $(this).parent().parent().attr("id");
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'acceptProposal',
        data: {
            task_key:task_key,
            sender:sender,
            state:1
        },
        success: function (response) {
            
            var res = $.trim(response);
            if(res=="Accepted"){
                task.children("#"+sender).children(".icons").empty();
                task.children("#"+sender).children(".icons").append("<i class='fa fa-check-circle-o' style='font-size:40px'></i>");
                button.parent().remove();
                button.prop('disabled', false);
            }
            
        },
        error: function(){
            button.prop('disabled', false);
        }
    });
    
    
});

$(".decline-proposal").on("click",function(){
    var button = $(this);
    button.prop('disabled', true);
    var task = $(this).parent().parent().parent();
    var task_key = task.attr("id"); 
    var sender = $(this).parent().parent().attr("id");
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'acceptProposal',
        data: {
            task_key:task_key,
            sender:sender,
            state:-1,
        },
        success: function (response) {
            
            var res = $.trim(response);
            if(res=="Accepted"){
                task.children("#"+sender).children(".icons").empty();
                task.children("#"+sender).children(".icons").append("<i class='fa fa-times-circle-o' style='font-size:40px'></i>");
                button.parent().remove();
                button.prop('disabled', false);
            }
            
        },
        error: function(){
            button.prop('disabled', false);
        }
    });
    
    
});



$(".accept-demend").on("click",function(){
    var button = $(this);
    button.prop('disabled', true);
    var task = $(this).parent().parent().parent();
    var task_key = task.attr("id"); 
    var developer_key = $(this).parent().parent().parent().parent().attr("id");
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'acceptProposal',
        data: {
            task_key:task_key,
            state:1,
            developer_key:developer_key,
        },
        success: function (response) {
            
            var res = $.trim(response);
            if(res=="Accepted"){
                task.children(".icons").empty();
                task.children(".icons").append("<i class='fa fa-check-circle-o' style='font-size:40px'></i>");
                button.parent().remove();
                button.prop('disabled', false);
                
            }
            
        },
        error: function(){
                button.prop('disabled', false);
        
        }
    });
    
    
});

$(".decline-demend").on("click",function(){
    var button = $(this);
    button.prop('disabled', true);
    var task = $(this).parent().parent().parent();
    var task_key = task.attr("id"); 
    var developer_key = $(this).parent().parent().parent().parent().attr("id");
    $.ajax({
        datatype: 'text/plain',
        type: 'post',
        url: 'acceptProposal',
        data: {
            task_key:task_key,
            developer_key:developer_key,
            state:-1,
        },
        success: function (response) {
            
            var res = $.trim(response);
            if(res=="Accepted"){
                task.children(".icons").empty();
                task.children(".icons").append("<i class='fa fa-times-circle-o' style='font-size:40px'></i>");
                button.parent().remove();
                button.prop('disabled', false);
            }
            
        },
        error: function(){
            button.prop('disabled', false);
        }
    });
    
    
});



$("#task_type").on("change", function(){
    $("#select-skill2 option").removeClass("hidden");
    $(".list-skill").empty();
    $("#select-skill2").val("");
    var type_id = $("#task_type option:selected").val();
    $("#select-skill2 option").not("."+type_id).addClass("hidden");
});


/********BACKOFFICE********/


function remove_type(param){
    var type = $.trim($(param).parent().parent().children("a.type-name").text());
    
    var rm = $(param).parent().parent("li");
    $.ajax({
        datatype:'text/plain',
        type: 'post',
        url: 'rmType',
        data: {
            type:type
        },
        success: function(response) {
            if(response==1){
                $("#err-td").parent().removeClass("req");
                $("#err-td").remove();
                rm.remove();
            }
        },
        error: function(){
                $("#err-td").remove();
                $(".title-add-skill").parent().parent().addClass("req-select");
                $(".title-add-skill").parent().append("<p id='err-td' align='center' style='margin-top : 30px'><font size=5>This type can't be deleted</font></p>");
                $("#err-td").parent().addClass("req");
        }
    });              
}

$("#submit-skill").on("click",function (e){
    var skill = $("#skill-name").val();
    var val = $("#typeS").val();
    if (val == "" && skill == ""){
        e.preventDefault();
        $("#typeS").addClass("req-select");
        $("#err-ss").remove();
        $("#skill-name").addClass("req");
        $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, fill the form</p>");
    } else if (val == ""){
        e.preventDefault();
            $("#skill-name").removeClass("req");
            $("#typeS").addClass("req-select");
       
            $("#err-ss").remove();
            $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, choose the skill's type</p>");
      
    }else if( skill ==""){
        e.preventDefault();
         $("#typeS").removeClass("req-select");
         $("#err-ss").remove();
         $("#skill-name").addClass("req");
         $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, choose the skill's name</p>");
        
    }else{
        $("#skill-name").removeClass("req");
        $("#typeS").children("a").removeClass("req-select");
        $("#err-ss").remove();
    }
});



$("#typeS").on("change",function (){
    var val = $("#typeS").val();
    if (val != ""){      
        $("#typeS").removeClass("req-select");
        $("#err-ss").remove();
    } else{
        
            $("#typeS").addClass("req-select");
       
            $("#err-ss").remove();
            $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, choose the skill's type</p>");
    }
});

$("#skill-name").on("change", function(){
    if( skill ==""){
       
         $("#err-ss").remove();
         $("#skill-name").addClass("req");
         $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, choose the skill's name</p>");
        
    } else {
        $("#skill-name").removeClass("req");
        $("#err-ss").remove();
    }
});

$("#skill-father").on("click", function(){
    var type =  $("#typeS").val();
    if (type == ""){
        $("#err-ss").remove();
        $("#typeS").addClass("req-select");
        $("#skill-submit").append("<p id='err-ss' style='margin-top : 10px'>Please, select a type before</p>");
        $("#typeS").focus();
    } else{
        $("#err-ss").remove();
        $("#typeS").removeClass("req-select");
    }
});



$("#delete-skill").on("click",function (e){
    var val = $("#rm-skill-b").val();
    if (val == ""){
        e.preventDefault();
            $("#rm-skill-b").addClass("req-select");
        
            $("#err-sd").remove();
            $("#skill-delete").append("<p id='err-sd' style='margin-top : 10px'>Please, choose the skill to delete</p>");
        
    }else{
        $("#rm-skill-b").removeClass("req-select");
        $("#err-sd").remove();
    }
});

$("#rm-skill-b").on("change",function (){
    var val = $("#rm-skill-b").val();
    if (val != ""){      
        $("#rm-skill-b").removeClass("req-select");
        $("#err-sd").remove();
    }else{
        $("#rm-skill-b").addClass("req-select");
        
            $("#err-sd").remove();
            $("#skill-delete").append("<p id='err-sd' style='margin-top : 10px'>Please, choose the skill to delete</p>");
    }
});

$("#update-skill").on("click",function (){
    var val = $("#old-skill").val();
    if (val == ""){
        $("#old-skill").addClass("req-select");
        
            $("#err-su").remove();
            $("#skill-update").append("<p id='err-su' style='margin-top : 10px'>Please, choose the skill to update</p>");
        
    }else{
        $("#old-skill").removeClass("req-select");
        $("#err-su").remove();
    }
});

$("#old-skill").on("change",function (){
    var val = $("#old-skill").val();
    if (val != ""){      
        $("#old-skill").removeClass("req-select");
        $("#err-su").remove();
    }
});

$("#form-update-skill").on("submit",function (e){
    var new_name = $("#new-skill-name").val();
    var skill_father = $("#new-father").val();
    var type = $("#new-type").val();
    if (new_name=="" && skill_father =="" && type ==""){
        
       
        e.preventDefault();
        
        $("#new-skill-name").addClass("req-select");
        $("#new-father").addClass("req-select");
        $("#new-type").addClass("req-select");
        $("#err-su2").remove();
        $("#skill-update").append("<p id='err-su2' style='margin-top : 10px'>Please, make at least one change</p>");
        
    }else{
        $("#new-skill-name").removeClass("req-select");
        $("#new-father").removeClass("req-select");
        $("#new-type").removeClass("req-select");
        $("#err-su2").remove();
    }
});

$("#new-skill-name , #new-father, #new-type").on("change",function (){
    var new_name = $("#new-skill-name").val();
    var skill_father = $("#new-father").val();
    var type = $("#new-type").val();
    if (new_name!="" || skill_father !="" || type !=""){   
        $("#new-skill-name").removeClass("req-select");
        $("#new-father").removeClass("req-select");
        $("#new-type").removeClass("req-select");
        $("#err-su2").remove();
    }
});

$("#new-father").on("click", function(){
    var new_type =  $("#new-type").val();
    if (new_type == ""){
        $("#err-su2").remove();
        $("#new-type").addClass("req-select");
        $("#skill-update").append("<p id='err-su2' style='margin-top : 10px'>Please, select a type before</p>");
        $("#new-type").focus();
    } else{
        $("#err-su2").remove();
        $("#new-type").removeClass("req-select");
    }
});

$("#type-name").on("change", function (e){
      var name = $("#type-name").val();
      if (name == ""){
        e.preventDefault();
        $("#err-ts").remove();
        $("#type-name").addClass("req");
        $("#type-submit").append("<p id='err-ts' style='margin-top : 10px'>Please, insert the type name</p>");
    } else {
        $("#err-ts").remove();
        $("#type-name").removeClass("req");
    }
});

$("#type-submit").on("click", function(e){
    var name = $("#type-name").val();
    if (name == ""){
        e.preventDefault();
        $("#err-ts").remove();
        $("#type-name").addClass("req");
        $("#type-submit").append("<p id='err-ts' style='margin-top : 10px'>Please, insert the type name</p>");
    } else {
        $("#err-ts").remove();
        $("#type-name").removeClass("req");
    }
});

$("#update-type").on("click",function (e){
    var old = $("#old-type").val();
    var mod = $("#mod-type").val();
    if (old == ""){
        e.preventDefault();
        $("#err-tu").remove();
        $("#old-type").addClass("req-select");
        $("#type-update").append("<p id='err-tu' style='margin-top : 10px'>Please, choose the type to update</p>");
        
    }else if (mod == ""){
        e.preventDefault();
        $("#err-tu").remove();
        $("#mod-type").addClass("req");
        $("#type-update").append("<p id='err-tu' style='margin-top : 10px'>Please, choose the skill's new name</p>");
        
    }else{
        $("#mod-type").removeClass("req");
        $("#old-type").removeClass("req-select");
        $("#err-tu").remove();
    }
});


$("#mod-type").on("change", function(){
    var mod_type = $("#mod-type").val();
    if (mod_type==""){
        $("#err-tu").remove();
         $("#mod-type").addClass("req");
         $("#type-update").append("<p id='err-tu' style='margin-top : 10px'>Please, insert the skill's new name</p>");
    } else {
        $("#err-tu").remove();
        $("#mod-type").removeClass("req");
    }
});

$("#old-type").on("change",function (){
    var val = $("#old-type").val();
    if (val != ""){      
        $("#old-type").removeClass("req-select");
        $("#err-tu").remove();
    }else{
        $("#err-tu").remove();
        $("#old-type").addClass("req-select");
        $("#type-update").append("<p id='err-tu' style='margin-top : 10px'>Please, choose the type to update</p>");
    }
});


//CHECKS ON VALUE


//signup checks
function isValidDateBirth(s) {
  
  var bits = s.split('/');
  var y = bits[2],
    m = bits[1],
    d = bits[0];
  
  var date = new Date(bits[2],bits[1],bits[0]); 
  var daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

  
  if ((!(y % 4) && y % 100) || !(y % 400)) {
    daysInMonth[1] = 29;
  }
  var today = new Date();
  today.setHours(0,0,0,0);
  
  if(date > today){
      return false;
  }
  
  return !(/\D/.test(String(d))) && d > 0 && d <= daysInMonth[--m];
}

function isGreaterDate(s,e){
    var bits1 = s.split('/');
    var y1 = bits1[2],
    m1 = bits1[1],
    d1 = bits1[0];
    var bits2 = e.split('/');
    var y2 = bits2[2],
    m2 = bits2[1],
    d2 = bits2[0];
  
    var start_date = new Date(y1,m1,d1);
    var end_date = new Date(y2,m2,d2);
    
    
    
    if (start_date > end_date){
        return true;
    }else{
        return false;
    }
}

function isValidDateTask(s) {
  
  var bits = s.split('/');
  var y = bits[2],
    m = bits[1],
    d = bits[0];
  
  var date = new Date(bits[2],bits[1],bits[0]); 
  var daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

  
  if ((!(y % 4) && y % 100) || !(y % 400)) {
    daysInMonth[1] = 29;
  }
  var today = new Date();
  today.setHours(0,0,0,0);
  
  if(date < today){
      return false;
  }
  
  return !(/\D/.test(String(d))) && d > 0 && d <= daysInMonth[--m];
}

$("#pwd2").on("change",function(){
   var pwd1 = $("#pwd1").val(),
   pwd2 = $("#pwd2").val();
   
   if ( pwd1 != pwd2){
       $("#pwd2").addClass("req");
       $("#pwd1").addClass("req");
       $("#err-pwd").remove();
       $("#pwd2").parent().append("<p id='err-pwd' style='margin-top : 10px'>Passwords don't match </p>");
   }else{
       
       $("#pwd2").removeClass("req");
       $("#pwd1").removeClass("req");
       $("#err-pwd").remove();
   }
});

$("#pwd1").on("change",function(){
   var pwd1 = $("#pwd1").val(),
   pwd2 = $("#pwd2").val();

if(pwd1 ==="") {  
   if ( pwd1 != pwd2){
       $("#pwd2").addClass("req");
       $("#pwd1").addClass("req");
       $("#err-pwd").remove();
       $("#pwd2").parent().append("<p id='err-pwd' style='margin-top : 10px'>Passwords don't match </p>");
   }else{
   
       $("#pwd2").removeClass("req");
       $("#pwd1").removeClass("req");
       $("#err-pwd").remove();
   }
}
});




$("#birthdate").on("change",function(){
   if($("#birthdate").val()!==""){
   
    if(!(isValidDateBirth($("#birthdate").val()))){
      $("#birthdate").addClass("req");
      
    }else{
      $("#birthdate").removeClass("req");
      
    }
   }
});

$("#registrati").on("submit", function(e){
    
    if($("#birthdate").val()!==""){
    if(!(isValidDateBirth($("#birthdate").val()))){
        e.preventDefault();
        $("#birthdate").addClass("req");
        
    }else{
        $("#birthdate").removeClass("req");
      
    }
    }else{
        e.preventDefault();
        $("#birthdate").addClass("req");
      }
    
   var pwd1 = $("#pwd1").val(),
   pwd2 = $("#pwd2").val();
   if ( pwd1 != pwd2){
       e.preventDefault();
       $("#pwd2").addClass("req");
       $("#pwd1").addClass("req");
       $("#err-pwd").remove();
       $("#pwd2").parent().append("<p id='err-pwd' style='margin-top : 10px'>Passwords don't match </p>");
   }else{
       $("#pwd2").removeClass("req");
       $("#pwd1").removeClass("req");
       $("#err-pwd").remove();
   }
    
});



//myskill checks
$("#add-button").on("click",function(){
    var skill = $("#select-skill").val();
    var level = $("#select-level").val();
    if(skill === "" && level ===""){
        $("#select_skill_chosen").children("a").addClass("req-select");
        $("#select_level_chosen").children("a").addClass("req-select");
        }else if(skill === ""){
        $("#select_skill_chosen").children("a").addClass("req-select");
        }else if(level === ""){
        $("#select_level_chosen").children("a").addClass("req-select");
    }else{
        $("#select_skill_chosen").children("a").removeClass("req-select");
        $("#select_level_chosen").children("a").removeClass("req-select");
        $("#err-addskill").remove();
    }
});

$("#select-skill").on("change",function(){
    var skill = $("#select-skill").val();
    var level = $("#select-level").val();
    if(skill === "" && level ===""){
        $("#select_skill_chosen").children("a").addClass("req-select");
        $("#select_level_chosen").children("a").addClass("req-select");
    }else if(skill === ""){
        $("#select_skill_chosen").children("a").addClass("req-select");
    }else{
        $("#select_skill_chosen").children("a").removeClass("req-select");
    }
});

$("#select-level").on("change",function(){
    var skill = $("#select-skill").val();
    var level = $("#select-level").val();
    if(skill === "" && level ===""){
        $("#select_skill_chosen").children("a").addClass("req-select");
        $("#select_level_chosen").children("a").addClass("req-select");
    }else if(skill === ""){
       $("#select_skill_chosen").children("a").addClass("req-select");
    }else if(level === ""){
        $("#select_level_chosen").children("a").addClass("req-select");
    }else{
        $("#select_level_chosen").children("a").removeClass("req-select");
        $("#err-addskill").remove();
    }
});


//create project checks

$("#task_name").on("change", function(){
    var task_name = $("#task_name").val();
    if(task_name === ""){
        $("#task_name").addClass("req");
        $("#err-tasN").remove();
        $("#task_name").parent().append("<p id='err-tasN' align = 'left'>Please, insert the task's type</p>");
    }else{
        $("#task_name").removeClass("req");
        $("#err-tasN").remove();
    }
});

$("#task_type").on("change", function(){
    var task_type = $("#task_type").val();
    if(task_type === ""){
        $("#task_type").addClass("req-select");
        $("#err-tasT").remove();
        $("#task_type").parent().parent().append("<p id='err-tasT' align='left'>Please, insert the task's type</p>");
    }else{
        $("#task_type").removeClass("req-select");
        $("#err-tasT").remove();
    }
});

$("#select-skill2").on("click", function(){
    var type =  $("#task_type").val();
    if (type == ""){
        $("#err-tasT").remove();
        $("#task_type").addClass("req-select");
        $("#task_type").parent().parent().append("<p id='err-tasT' style='margin-top : 10px'>Please, select a type before</p>");
        $("#task_type").focus();
    } else{
        $("#err-tasT").remove();
        $("#task_type").removeClass("req-select");
    }
});


$("#start_date").on("change",function(){
   var start_date = $("#start_date").val();
   if(start_date!==""){
   
    if(!(isValidDateTask(start_date))){
      $("#start_date").addClass("req");
      $("#err-taskSD").remove();
      $("#start_date").parent().append("<p id='err-taskSD' align='left'>The task's start date is uncorrect</p>");
      
    }else if($("#end_date").val()!=="" && (isGreaterDate(start_date,$("#end_date").val()))){
      
        $("#start_date").addClass("req");
        $("#end_date").addClass("req");
        $("#err-taskSD").remove();
        $("#err-taskED").remove();
        $("#start_date").parent().append("<p id='err-taskSD' align='left'>A task can't end before it starts</p>");
      }else{ 
      
      $("#start_date").removeClass("req");
      $("#err-taskSD").remove();
      if(isValidDateTask($("#end_date").val())){
          $("#end_date").removeClass("req");
          $("#err-taskED").remove();
      }
    }
   }
});

$("#end_date").on("change",function(){
   var end_date = $("#end_date").val();
   if(end_date!==""){
   
    if(!(isValidDateTask(end_date))){
      $("#end_date").addClass("req");
      $("#err-taskED").remove();
      $("#end_date").parent().append("<p id='err-taskED' align='left'>The task's end date is uncorrect</p>");
      
    }else if($("#start_date").val()!=="" && (isGreaterDate($("#start_date").val(),end_date))){
      
        $("#start_date").addClass("req");
        $("#end_date").addClass("req");
        $("#err-taskSD").remove();
        $("#err-taskED").remove();
        $("#start_date").parent().append("<p id='err-taskSD' align='left'>A task can't end before it starts</p>");
      }else{ 
      
      $("#end_date").removeClass("req");
      $("#err-taskED").remove();
      
      if(isValidDateTask($("#start_date").val())){
          $("#start_date").removeClass("req");
          $("#err-taskSD").remove();
      }
    }
   }
});

$("#num_collaborators").on("change", function(){
    
    var task_coll = $("#num_collaborators").val();
    if(task_coll === "" || Number(task_coll)<1){
        $("#num_collaborators").addClass("req");
        $("#err-tasC").remove();
        $("#num_collaborators").parent().parent().append("<p id='err-tasC' align='left'>Please, insert a value greater than 0</p>");
    }else{
        $("#num_collaborators").removeClass("req");
        $("#err-tasC").remove();
    }
});


$("#select-skill2").on("change",function(){
    var skill = $("#select-skill2").val();
    var level = $("#select-level2").val();
    if(skill === "" && level ===""){
        $("#select-skill2").addClass("req-select");
        $("#select-level2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill and the relative level</p>");
    }else if(skill === ""){
        $("#select-skill2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill</p>");
    }else{
        $("#select-skill2").removeClass("req-select");
        $("#err-tasSK").remove();
    }
});

$("#select-level2").on("change",function(){
    var skill = $("#select-skill2").val();
    var level = $("#select-level2").val();
    if(skill === "" && level ===""){
        $("#select-skill2").addClass("req-select");
        $("#select-level2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill and the relative level</p>");
    }else if(skill === ""){
        $("#select-skill2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill</p>");
    }else if(level === ""){
        $("#select-level2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, select a level to the skill</p>");
    }else{
        $("#err-tasSK").remove();
        $("#select-skill2").removeClass("req-select");
        $("#select-level2").removeClass("req-select");
    }
});

$("#add-button2").on("click",function(){
    var skill = $("#select-skill2").val();
    var level = $("#select-level2").val();
    if(skill === "" && level ===""){
       
        $("#err-tasSK").remove();
        $("#select-skill2").addClass("req-select");
        $("#select-level2").addClass("req-select");
        $("#list-skill").append("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill and the relative level</p>");
    }else if(skill === ""){
        
        $("#select-skill2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").prepend("<p id='err-tasSK' style='margin-top : 10px'>Please, choose a skill</p>");
    }else if(level === ""){
        
        $("#select-level2").addClass("req-select");
        $("#err-tasSK").remove();
        $("#list-skill").prepend("<p id='err-tasSK' style='margin-top : 10px'>Please, select a level to the skill</p>");
    }else{
        $("#err-tasSK").remove();
        $("#select-skill2").removeClass("req-select");
        $("#select-level2").removeClass("req-select");
    }
    
});


$("#submit-task").on("click",function(e){
   var task_name = $("#task_name").val(),
   task_type = $("#task_type option:selected").text(),

   start_date = $("#start_date").val(),
   end_date = $("#end_date").val(),
   task_descr = $("#task_descr").val(),
   task_coll = $("#num_collaborators").val();
   var isOpen = $("#isOpen").val();
   var skill_level = [];
   if (task_name == "" || task_type == "Select Type" ||  start_date == "" ||   end_date == "" || task_descr == ""  || task_coll=="" || ($('ul#list-skill li').length < 1)){
      
       if(task_name === ""){
            $("#task_name").addClass("req");
            $("#err-tasN").remove();
            $("#task_name").parent().append("<p id='err-tasN' align = 'left'>Please, insert the task's type</p>");
        }
      
    if(task_type === "Select Type"){
        $("#task_type").addClass("req-select");
        $("#err-tasT").remove();
        $("#task_type").parent().parent().append("<p id='err-tasT' align='left'>Please, insert the task's type</p>");
    }
      
    if(start_date!==""){
   
    if(!(isValidDateTask(start_date))){
      $("#start_date").addClass("req");
      $("#err-taskSD").remove();
      $("#start_date").parent().append("<p id='err-taskSD' align='left'>The task's start date is uncorrect</p>");
      
    }else if($("#end_date").val()!=="" && (isGreaterDate(start_date,$("#end_date").val()))){
      
        $("#start_date").addClass("req");
        $("#end_date").addClass("req");
        $("#err-taskSD").remove();
        $("#err-taskED").remove();
        $("#start_date").parent().append("<p id='err-taskSD' align='left'>A task can't end before it starts</p>");
      }
      
      
    } else{
      $("#start_date").addClass("req");
      $("#err-taskSD").remove();
      $("#start_date").parent().append("<p id='err-taskSD' align='left'>Please, insert the start_date</p>");
    }   
    if(end_date!==""){
   
    if(!(isValidDateTask(end_date))){
      $("#end_date").addClass("req");
      $("#err-taskED").remove();
      $("#end_date").parent().append("<p id='err-taskED' align='left'>The task's end date is uncorrect</p>");
      
    }
    }else{
      $("#end_date").addClass("req");
      $("#err-taskED").remove();
      $("#end_date").parent().append("<p id='err-taskED' align='left'>Please, insert the end date</p>");
    }if(task_coll === "" || Number(task_coll)<1){
        $("#num_collaborators").addClass("req");
        $("#err-tasC").remove();
        $("#num_collaborators").parent().parent().append("<p id='err-tasC' align='left'>Please, insert a value greater than 0</p>");
    }
      if ($('ul#list-skill li').length < 1){
            $("#err-tasSK").remove();
            $("#select-skill2").addClass("req-select");
            $("#select-level2").addClass("req-select");
            $("#list-skill").append("<p id='err-tasSK' align='left'>Please, insert at least on skill</p>");
    }
     e.preventDefault();
   }else{
       $("#err-projT").remove();
       $(".list-skill").children("li").each(function (){
          var skill_name = $.trim($(this).children("a.skill-name").text());
          var level = $.trim($(this).children("a#skill-level").text());
          skill_level.push(skill_name+":"+level);
            });
            
        $("#popup1").addClass("hidden");
        aggiungi_task(task_name,start_date,end_date,task_descr, task_coll, skill_level,task_type,isOpen);
    
   }
}); 
 
   $("#project_name").on("change",function(){
       var project_name = $("#project_name").val();
       if (project_name == ""){
        $("#project_name").addClass("req");
        $("#err-proj").remove();
        $("#project_name").parent().append("<p id='err-proj' align='left' style='margin-bottom : 20px'>Please, insert the project name</p>");
       }else{
         $("#project_name").removeClass("req");  
         $("#err-proj").remove();
       }
   });
  
   $("#submit-project").on("click",function(e){
       var project_name = $("#project_name").val();
       if( project_name == "" || $('ul#task-list li').length < 1){
           e.preventDefault();
           if(project_name == ""){
               $("#project_name").addClass("req");
               $("#err-proj").remove();
               $("#project_name").parent().append("<p id='err-proj' align='left' style='margin-bottom : 20px'>Please, insert the project name</p>");
           }
           if($('ul#task-list li').length < 1){
               $("#err-projT").remove();
               $("#task-list").parent().append("<p id='err-projT' align='center' style='margin-top : 20px'><font size=6>Please, insert at least a task</font></p>");
           }
       }else{
           //azzero lavore input nascosto
    $("#tasks").val("");
    //conto il numero di task aggiunti
    var c = $(".task-aggiunti").children("li").length;
    var i=0;
    for(i=0;i<c;i++){
        //controlliamo valore dell'input nascosto
        var input_tasks = $("#tasks").val();
        //recuperiamo tutti i valori del task
        var task = $(".task-aggiunti").children("li")[i];
        var name = $.trim($(task).children().children("p#name").text().split(":")[1]);
        var isOpen = $.trim($(task).children().children("p#isOpen").text().split(":")[1]);
        var tipo = $.trim($(task).children().children("p#tipo").text().split(":")[1]);
        var start = $.trim($(task).children().children("p#start-end").text().split(":")[1].split("-")[0]);
        var end = $.trim($(task).children().children("p#start-end").text().split("-")[1].split(":")[1]);
        var descr = $.trim($(task).children().children("p#descr").text().split(":")[1]);
        var coll = $.trim($(task).children().children("p#coll").text().split(":")[1]);
        var skills = $.trim($(task).children().children("p.skills").text().split(":")[1]);
        if(input_tasks==""){
            $("#tasks").val(name+"#"+start+"#"+end+"#"+descr+"#"+coll+"#"+skills+"#"+tipo+"#"+isOpen+"@");
        }else{
            $("#tasks").val(input_tasks+name+"#"+start+"#"+end+"#"+descr+"#"+coll+"#"+skills+"#"+tipo+"#"+isOpen+"@");
        }
        
    }
       }
   });
   
  
$("#typeS").on("change", function(){
    $("#skill-father option").removeClass("hidden");
    $("#skill-father").val("");
    var type_id = $("#typeS option:selected").val();
    $("#skill-father option").not("."+type_id).addClass("hidden");
});

$("#new-type").on("change", function(){
    $("#new-father option").removeClass("hidden");
    $("#new-father").val("");
    var type_id = $("#new-type option:selected").val();
    $("#new-father option").not("."+type_id).addClass("hidden");
});