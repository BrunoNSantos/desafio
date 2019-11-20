import { ClrDatagridStateInterface } from '@clr/angular';
import { HttpParams } from '@angular/common/http';

export default class ClrDatagridUtils {
    static stateToQueryParams(state: ClrDatagridStateInterface): HttpParams {
        let params = new HttpParams();

        if (state) {
            if (state.page) {
                const pageNumber = Math.floor(state.page.from / state.page.size);
                params = params.append('size', String(state.page.size));
                params = params.append('page', String(pageNumber));
            }
            if (state.sort) {
                if (state.sort.by) {
                    let sort = String(state.sort.by);
                    if (state.sort.reverse) {
                        sort += ',desc';
                    } else {
                        sort += ',asc';
                    }
                    params = params.append('sort', sort);
                }
            }
        }

        return params;
    }
}
