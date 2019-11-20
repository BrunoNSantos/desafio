import { Component } from '@angular/core';
import {AppService} from './app.service';
import {ClrDatagridSortOrder, ClrDatagridStateInterface} from '@clr/angular';
import {ToasterService} from 'angular2-toaster';
import {Toast} from 'angular2-toaster/src/toast';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public title = 'Proposta';
  public propostas: [];
  public total: number;
  public carregando = true;
  public item: any = null;
  public direcaoOrdenacao = ClrDatagridSortOrder.ASC;
  public abrirModalCriarProposta = false;
  public abrirModalAprovarProposta = false;
  private toasterService: ToasterService;

  public nomeCliente: string;
  public cpf: string;
  public email: string;
  public renda: number;

  constructor(private appService: AppService, private toastr: ToastrService) {

  }

  async listarPropostas(state: ClrDatagridStateInterface) {
    try {
      const response: any = await this.appService.listarPropostas(state);
      if (response) {
        this.propostas = response;
        this.total = response.length;

      }
    } finally {
      this.item = null;
      this.carregando = false;
    }
  }

  async criarProposta() {
    const proposta = {
      'nomeCliente': this.nomeCliente,
      'cpf': this.cpf,
      'email': this.email,
      "renda": this.renda,
    };

    this.appService.criarProposta(proposta).then(value => {
      this.abrirModalCriarProposta = false;
      this.listarPropostas(null);
      this.toastr.success('Proposta Criada', 'Sucesso');
    });
  }

  aprovarProposta() {
    this.appService.aprovarProposta(this.item.id).then(value => {
      this.abrirModalAprovarProposta = false;
      this.listarPropostas(null);
      this.toastr.success('Proposta Aprovada', 'Sucesso');
    });
  }
}
