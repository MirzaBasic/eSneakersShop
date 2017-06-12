namespace eSneakers.MVC.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class test4 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Narudzbas", "BrojNarudzbe", c => c.String());
            AddColumn("dbo.Proizvods", "Sifra", c => c.String());
            AddColumn("dbo.Proizvods", "Akcija", c => c.Boolean(nullable: false));
            AddColumn("dbo.Proizvods", "Popust", c => c.Int(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Proizvods", "Popust");
            DropColumn("dbo.Proizvods", "Akcija");
            DropColumn("dbo.Proizvods", "Sifra");
            DropColumn("dbo.Narudzbas", "BrojNarudzbe");
        }
    }
}
