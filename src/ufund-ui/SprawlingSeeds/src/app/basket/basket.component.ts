import { Component , OnInit} from '@angular/core';
import { Basket } from '../basket';
import { Need } from '../need';
import { BasketService } from '../basket.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-basket',
  standalone: false,
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})
export class BasketComponent implements OnInit {
  basket?: Basket;
  errorMessage?: string;
  total : number = 0;

  constructor (private basketService : BasketService, private router : Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    
  
  }

 
  ngOnInit(): void{
    this.getBasket();
  }

  getBasket(): void {
    this.basketService.getBasket()
    .subscribe(basket => this.basket = basket);
  }

  checkout(): void{
    this.basketService.checkoutBasket().subscribe({
      next: () => {
        console.log("ok");
        this.getBasket();
        this.router.navigateByUrl(this.router.url);
      },
      error: (e) => {
        this.errorMessage = "Failed to checkout entire basket";
        //console.log(e);
        this.getBasket();
      }
    }
    );
    
  }

  updateBasket(): void {
    if (this.basket == undefined) {
      return;
    }
    this.basketService.updateBasket(this.basket).subscribe();
  }

  incrementNeed(need: Need) : void {
    need.quantity = need.quantity + 1;
    this.updateBasket();
  }

  decrementNeed(need: Need): void {
    need.quantity = need.quantity - 1;
    if(need.quantity < 0) need.quantity = 0;
    this.updateBasket();
  }

  removeNeed(need: Need): void {
    if (this.basket == undefined) {
      return;
    }
    need.quantity = 0;
    this.basket.needs = this.basket.needs.filter(n => n !== need);
    this.updateBasket();
  }

  totalCost(need: Need): number{
    if(this.basket?.needs){
      this.total = this.basket.needs.reduce((total, need) =>{
        return total += (need.cost * need.quantity);
      }, 0);
    }
      return 0;
  }

  isValid(): boolean {
    return this.basketService.isLoggedIn() && !this.basketService.isAdmin();
  }
}
