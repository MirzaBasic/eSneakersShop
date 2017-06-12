namespace eSneakers.MVC.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class ende1 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Proizvods", "Ocjena", c => c.Single(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Proizvods", "Ocjena");
        }
    }
}
