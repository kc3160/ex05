import { Component, OnInit } from '@angular/core';
import {NeedsService} from '../needs.service';
import { Need, ToolNeed, FertilizerNeed, SeedNeed, BundleNeed } from '../need';
import { UserService } from '../user.service';
@Component({
  selector: 'app-needs',
  standalone: false,
  templateUrl: './needs.component.html',
  styleUrl: './needs.component.css'
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  typeSelected: string = '';

  newNeed!: Need;
  
  constructor (private needsService: NeedsService,
              private userService: UserService){ }

  ngOnInit(): void{
    this.getNeeds();
  }

  isAdmin(): boolean{
    return this.userService.isAdmin;
  }

  getNeeds(): void{
    this.needsService.getNeeds()
    .subscribe(n => this.needs = n);
  }

  onTypeChange(): void{
    switch (this.typeSelected) {
      case 'tool':
        this.newNeed = {
          type: 'tool',
          id: 1,
          name: '',
          quantity: 0,
          cost: 0,
          used: false, 
        } as ToolNeed;
        break;
      case 'fertilizer':
        this.newNeed = {
          type: 'fertilizer',
          id: 1,
          name: '',
          quantity: 0,
          cost: 0,
          organic: false, 
        } as FertilizerNeed;
        break;
      case 'seed':
        this.newNeed = {
          type: 'seed',
          id: 1,
          name: '',
          quantity: 0,
          cost: 0,
        } as SeedNeed;
        break;
      case 'bundle':
        this.newNeed = {
          type: 'bundle',
          id: 1,
          name: '',
          quantity: 0,
          cost: 0,
          discount: 0,
          needs: new Map<number,number>()
      } as BundleNeed;
      break;
      default:
        break;
    }
  }

  delete(need: Need): void{
    this.needs = this.needs.filter(n => n !==need);
    this.needsService.deleteNeed(need.id).subscribe();
  }

  addNeed() : void{
    this.needsService.addNeed(this.newNeed).subscribe(n => this.needs.push(n));
    this.typeSelected = '';
  }

  asFert(need: Need): FertilizerNeed{
    return need as FertilizerNeed;
  }

  asTool(need: Need): ToolNeed{
    return need as ToolNeed;
  }

  asBun(need: Need): BundleNeed {
    return need as BundleNeed;
  }

  needById(index: number, need: Need): number{
    return need?.id;
  }
}
