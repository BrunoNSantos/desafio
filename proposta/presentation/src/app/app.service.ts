import {HttpClient} from '@angular/common/http';
import {ClrDatagridStateInterface} from '@clr/angular';
import ClrDatagridUtils from './helper/clrDatagridUtils';

export class AppService {
  constructor(private http: HttpClient) { }

  listarPropostas(state: ClrDatagridStateInterface) {
    const queryParams = ClrDatagridUtils.stateToQueryParams(state);
    return new Promise((resolve, reject) => {
      this.http.post('http://localhost:8080/proposta/listarPropostas', {},{ params: queryParams })
        .subscribe(data => {
          resolve(data);
        }, error => {
          reject(error);
        });
    });
  }

  criarProposta(params) {
    return new Promise((resolve, reject) => {
      this.http.post('http://localhost:8080/proposta/criar', params)
        .subscribe(data => {
          resolve(data);
        }, error => {
          reject(error);
        });
    });
  }

  aprovarProposta(idProposta) {
    return new Promise((resolve, reject) => {
      this.http.put('http://localhost:8080/proposta/aprovar/' + idProposta, null)
        .subscribe(data => {
          resolve(data);
        }, error => {
          reject(error);
        });
    });
  }
}
