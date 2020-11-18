import { AuthService } from 'src/app/services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert';

@Component({
  selector: 'app-authorities',
  templateUrl: './authorities.component.html',
  styleUrls: ['./authorities.component.scss']
})
export class AuthoritiesComponent implements OnInit {
  authorities: any;
  loader: any
  descriptio: any;

  constructor(private authServices: AuthService) { }

  ngOnInit() {
    this.getAuthorities()
  }
  getAuthorities() {
    this.loader = false;
    this.authServices.getAuthority().subscribe(res => {
      this.authorities = res;
      this.loader = true;
      console.log(this.authorities);
    })
  }
  // get description
  getDescription(id, name) {
    this.authServices.getAuthorityDescription(id, name).subscribe(res => {
      console.log(res);
    })
  }
  // Code For search
  onKeydownEvent() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }
}
