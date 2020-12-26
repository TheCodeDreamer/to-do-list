let colorInput = document.querySelector('#color');

colorInput.addEventListener('input', () =>{
   let url = "http://localhost:8080/todolist?color=";
   let color = colorInput.value;
   let newcolor = document.querySelector(".right");
   let id = newcolor.id;
   console.log(id);
   newcolor.style.backgroundColor=color;
   let xhr = new XMLHttpRequest();
   url+= color.substring(1,color.length);
   url+="&namelist=";
   url+=id;
   console.log(url);
   xhr.open('GET', url, true);
   xhr.onload = function() {
      console.log(xhr.responseText);
   };
   xhr.send();
});

