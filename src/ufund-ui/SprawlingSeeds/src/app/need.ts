export interface Need{
 type: string;
 id: number;
 name: string;
 quantity: number;
 cost: number;
}


export interface ToolNeed extends Need{
 used: boolean;
}


export interface FertilizerNeed extends Need{
 organic: boolean;
}

export interface SeedNeed extends Need{
}

export interface BundleNeed extends Need{
  discount: number;
  needs: Map<number,number>;
}
